package fr.faction.webmap;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;

import java.lang.reflect.Method;
import java.util.*;

/**
 * Construit et envoie le snapshot global des factions + stats des joueurs.
 *
 * Interopère avec le FactionPlugin via réflexion pour ne pas créer de
 * dépendance hard au moment de la compilation — ce plugin peut tourner
 * même sans FactionPlugin installé (il n'enverra juste pas de snapshot).
 *
 * Structure du snapshot envoyé au site :
 * {
 *   factions: [
 *     {
 *       name, rank, power, chef,
 *       members: [uuid, ...],
 *       allies:  [name, ...],
 *       spawnCx, spawnCz,         // chunk coords du spawn faction (ou null)
 *       centerCx, centerCz        // barycentre des claims (calculé ici)
 *     }
 *   ],
 *   claims: [
 *     { faction, cx, cz, world }
 *   ],
 *   homes: [
 *     { uuid, name, cx, cz, world }
 *   ]
 * }
 */
public class SnapshotPusher {

    private final FactionWebMapPlugin plugin;
    private final WebMapAPI api;

    // Cache du plugin Faction pour la réflexion
    private Object factionPluginInstance = null;
    private boolean checkedFactionPlugin = false;

    public SnapshotPusher(FactionWebMapPlugin plugin, WebMapAPI api) {
        this.plugin = plugin;
        this.api    = api;
    }

    public void pushAll() {
        pushSnapshot();
        pushPlayerStats();
    }

    // ── Snapshot ──────────────────────────────────────────────────────────────────

    private void pushSnapshot() {
        Object factionPlugin = getFactionPlugin();
        if (factionPlugin == null) return;

        try {
            Map<String, Object> snapshot = buildSnapshot(factionPlugin);
            api.pushSnapshot(snapshot);
        } catch (Exception e) {
            if (plugin.getConfig().getBoolean("debug")) {
                plugin.getLogger().warning("[WebMap] Erreur snapshot : " + e.getMessage());
            }
        }
    }

    private Map<String, Object> buildSnapshot(Object fp) throws Exception {
        Object factionManager = call(fp, "getFactionManager");
        Object homeManager    = callSafe(fp, "getHomeManager");
        Object powerManager   = callSafe(fp, "getPowerManager");

        // Toutes les factions
        @SuppressWarnings("unchecked")
        Map<String, Object> allFactions = (Map<String, Object>) call(factionManager, "getAllFactions");

        List<Map<String, Object>> factionsOut = new ArrayList<>();
        List<Map<String, Object>> claimsOut   = new ArrayList<>();

        // Claims via ClaimManager
        Object claimManager = callSafe(fp, "getClaimManager");
        if (claimManager != null) {
            @SuppressWarnings("unchecked")
            Map<Object, Object> allClaims = (Map<Object, Object>) call(claimManager, "getAllClaims");
            if (allClaims != null) {
                for (Map.Entry<Object, Object> e : allClaims.entrySet()) {
                    Object key  = e.getKey();
                    Object data = e.getValue();
                    String facName = (String) call(data, "getFactionName");
                    int cx   = (int) call(key, "cx");
                    int cz   = (int) call(key, "cz");
                    String w = (String) callSafe(key, "world");
                    if (w == null) w = "world";
                    claimsOut.add(Map.of("faction", facName, "cx", cx, "cz", cz, "world", w));
                }
            }
        }

        // Calculer le barycentre des claims par faction
        Map<String, int[]> claimSums   = new HashMap<>();
        Map<String, Integer> claimCounts = new HashMap<>();
        for (Map<String, Object> claim : claimsOut) {
            String facName = (String) claim.get("faction");
            int cx = (int) claim.get("cx"), cz = (int) claim.get("cz");
            claimSums.merge(facName.toLowerCase(),
                new int[]{cx, cz}, (a, b) -> new int[]{a[0] + b[0], a[1] + b[1]});
            claimCounts.merge(facName.toLowerCase(), 1, Integer::sum);
        }

        // Construire les objets faction
        for (Map.Entry<String, Object> e : allFactions.entrySet()) {
            Object faction = e.getValue();
            String name    = (String) call(faction, "getName");
            Object chef    = call(faction, "getChef");   // UUID
            @SuppressWarnings("unchecked")
            List<Object> members = (List<Object>) call(faction, "getMembers");
            @SuppressWarnings("unchecked")
            Set<String> allies   = (Set<String>) call(faction, "getAllies");

            // Power & rank via FactionPowerManager
            double power = 0;
            String rank  = "Pierre";
            if (powerManager != null) {
                Object p = callSafe(powerManager, "getFactionPower", String.class, name);
                if (p != null) power = (double) p;
                Object r = callSafe(powerManager, "getFactionRank", String.class, name);
                if (r != null) rank = r.toString().replace("FactionRank.", "");
            }

            // Spawn
            Integer spawnCx = null, spawnCz = null;
            Object spawn = callSafe(faction, "getFactionSpawn");
            if (spawn instanceof Location loc) {
                spawnCx = loc.getBlockX() >> 4;
                spawnCz = loc.getBlockZ() >> 4;
            }

            // Barycentre claims
            Integer centerCx = null, centerCz = null;
            int[] sums = claimSums.get(name.toLowerCase());
            Integer cnt = claimCounts.get(name.toLowerCase());
            if (sums != null && cnt != null && cnt > 0) {
                centerCx = sums[0] / cnt;
                centerCz = sums[1] / cnt;
            }

            Map<String, Object> facOut = new LinkedHashMap<>();
            facOut.put("name",       name);
            facOut.put("chef",       chef != null ? chef.toString() : null);
            facOut.put("rank",       rank);
            facOut.put("power",      power);
            facOut.put("members",    members != null ? members.stream().map(Object::toString).toList() : List.of());
            facOut.put("allies",     allies != null ? new ArrayList<>(allies) : List.of());
            facOut.put("spawnCx",    spawnCx);
            facOut.put("spawnCz",    spawnCz);
            facOut.put("centerCx",   centerCx);
            facOut.put("centerCz",   centerCz);
            factionsOut.add(facOut);
        }

        // Homes
        List<Map<String, Object>> homesOut = new ArrayList<>();
        if (homeManager != null) {
            // getHomes(UUID) pour chaque joueur en ligne
            for (org.bukkit.entity.Player player : Bukkit.getOnlinePlayers()) {
                try {
                    @SuppressWarnings("unchecked")
                    List<Object> homes = (List<Object>) callSafe(homeManager, "getHomes",
                            java.util.UUID.class, player.getUniqueId());
                    if (homes == null) continue;
                    for (Object h : homes) {
                        String homeName = (String) call(h, "name");  // champ public
                        Location loc    = (Location) getField(h, "location");
                        if (loc == null) continue;
                        homesOut.add(Map.of(
                            "uuid",  player.getUniqueId().toString(),
                            "name",  homeName,
                            "cx",    loc.getBlockX() >> 4,
                            "cz",    loc.getBlockZ() >> 4,
                            "world", loc.getWorld().getName()
                        ));
                    }
                } catch (Exception ignored) {}
            }
        }

        return Map.of("factions", factionsOut, "claims", claimsOut, "homes", homesOut);
    }

    // ── Stats joueurs ─────────────────────────────────────────────────────────────

    private void pushPlayerStats() {
        Object fp = getFactionPlugin();
        Object statsManager  = fp != null ? callSafe(fp, "getStatsManager")  : null;
        Object powerManager  = fp != null ? callSafe(fp, "getPowerManager")  : null;
        Object factionManager= fp != null ? callSafe(fp, "getFactionManager"): null;

        for (org.bukkit.entity.Player player : Bukkit.getOnlinePlayers()) {
            try {
                String uuid   = player.getUniqueId().toString();
                String pseudo = player.getName();

                Map<String, Object> stats = new LinkedHashMap<>();

                // Faction
                if (factionManager != null) {
                    Object faction = callSafe(factionManager, "getPlayerFaction",
                            java.util.UUID.class, player.getUniqueId());
                    if (faction != null) {
                        stats.put("faction", call(faction, "getName"));
                    }
                }

                // Power
                if (powerManager != null) {
                    Object power = callSafe(powerManager, "getPlayerPower",
                            java.util.UUID.class, player.getUniqueId());
                    if (power != null) stats.put("power", power);
                }

                // Stats (kills, deaths, playtime via PlayerStatsManager)
                if (statsManager != null) {
                    Object s = callSafe(statsManager, "getStats",
                            java.util.UUID.class, player.getUniqueId());
                    if (s != null) {
                        Object kills    = callSafe(s, "getKills");
                        Object deaths   = callSafe(s, "getMorts");
                        Object playtime = callSafe(s, "getPlaytimeMs");
                        if (kills    != null) stats.put("kills",     kills);
                        if (deaths   != null) stats.put("deaths",    deaths);
                        if (playtime != null) stats.put("playtimeMs", playtime);
                    }
                }

                api.pushStats(uuid, pseudo, stats);
            } catch (Exception e) {
                if (plugin.getConfig().getBoolean("debug")) {
                    plugin.getLogger().warning("[WebMap] stats " + player.getName() + " : " + e.getMessage());
                }
            }
        }
    }

    // ── Réflexion helpers ─────────────────────────────────────────────────────────

    private Object getFactionPlugin() {
        if (!checkedFactionPlugin) {
            checkedFactionPlugin = true;
            factionPluginInstance = Bukkit.getPluginManager().getPlugin("FactionPlugin");
            if (factionPluginInstance == null) {
                plugin.getLogger().warning("[WebMap] FactionPlugin non trouvé — snapshot désactivé.");
            }
        }
        return factionPluginInstance;
    }

    private static Object call(Object target, String methodName, Class<?>... argTypes) throws Exception {
        if (target == null) return null;
        Method m = findMethod(target.getClass(), methodName, argTypes);
        if (m == null) throw new NoSuchMethodException(target.getClass().getSimpleName() + "#" + methodName);
        m.setAccessible(true);
        return m.invoke(target);
    }

    private static Object callSafe(Object target, String methodName, Class<?>... argTypes) {
        try { return call(target, methodName, argTypes); } catch (Exception e) { return null; }
    }

    private static Object callSafe(Object target, String methodName, Class<?> argType, Object arg) {
        try {
            if (target == null) return null;
            Method m = findMethod(target.getClass(), methodName, argType);
            if (m == null) return null;
            m.setAccessible(true);
            return m.invoke(target, arg);
        } catch (Exception e) { return null; }
    }

    private static Method findMethod(Class<?> clazz, String name, Class<?>... argTypes) {
        for (Class<?> c = clazz; c != null; c = c.getSuperclass()) {
            try { return c.getDeclaredMethod(name, argTypes); } catch (NoSuchMethodException ignored) {}
        }
        return null;
    }

    private static Object getField(Object target, String fieldName) {
        try {
            var f = target.getClass().getDeclaredField(fieldName);
            f.setAccessible(true);
            return f.get(target);
        } catch (Exception e) { return null; }
    }
}
