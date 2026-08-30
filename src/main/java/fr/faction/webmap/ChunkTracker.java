package fr.faction.webmap;

import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Suit les chunks visités par chaque joueur.
 *
 * - À chaque changement de chunk (PlayerMoveEvent sur changement de chunk),
 *   on ajoute (cx, cz) au buffer en mémoire du joueur.
 * - Un flush périodique (toutes les 30s) envoie les nouveaux chunks via WebMapAPI.
 * - La base de données (INSERT IGNORE) garantit la déduplication côté serveur.
 *
 * Overworld uniquement (monde "world"). Le Nether et The End sont ignorés
 * par défaut (configurable via worlds-tracked dans config.yml).
 */
public class ChunkTracker implements Listener {

    private final FactionWebMapPlugin plugin;
    private final WebMapAPI api;

    // UUID → set de "cx,cz" des chunks vus depuis le dernier flush
    private final Map<UUID, Set<String>> pending = new ConcurrentHashMap<>();

    // UUID → (cx, cz) du dernier chunk connu (pour détecter un changement)
    private final Map<UUID, long[]> lastChunk = new ConcurrentHashMap<>();

    // Worlds à tracker (depuis config)
    private final Set<String> trackedWorlds;

    public ChunkTracker(FactionWebMapPlugin plugin, WebMapAPI api) {
        this.plugin = plugin;
        this.api    = api;
        this.trackedWorlds = new HashSet<>(
            plugin.getConfig().getStringList("worlds-tracked")
        );
        if (trackedWorlds.isEmpty()) trackedWorlds.add("world");
    }

    // ── Events ────────────────────────────────────────────────────────────────────

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onMove(PlayerMoveEvent event) {
        // Optimisation : ne traiter que si le chunk a changé
        if (event.getFrom().getChunk().equals(event.getTo().getChunk())) return;
        trackChunk(event.getPlayer(), event.getTo().getChunk());
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent event) {
        trackChunk(event.getPlayer(), event.getTo().getChunk());
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        trackChunk(event.getPlayer(), event.getPlayer().getLocation().getChunk());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        // Flush immédiat à la déconnexion
        UUID uuid = event.getPlayer().getUniqueId();
        String pseudo = event.getPlayer().getName();
        flush(uuid, pseudo, event.getPlayer().getWorld().getName());
        pending.remove(uuid);
        lastChunk.remove(uuid);
    }

    // ── Core ─────────────────────────────────────────────────────────────────────

    private void trackChunk(Player player, Chunk chunk) {
        String worldName = chunk.getWorld().getName();
        if (!trackedWorlds.contains(worldName)) return;

        int cx = chunk.getX(), cz = chunk.getZ();

        // Vérifier si le chunk a changé
        UUID uuid = player.getUniqueId();
        long[] last = lastChunk.get(uuid);
        long packed = ((long) cx << 32) | (cz & 0xFFFFFFFFL);
        if (last != null && last[0] == packed) return;
        lastChunk.computeIfAbsent(uuid, k -> new long[1])[0] = packed;

        // Ajouter au buffer
        pending.computeIfAbsent(uuid, k -> ConcurrentHashMap.newKeySet())
               .add(cx + "," + cz);
    }

    // ── Flush ─────────────────────────────────────────────────────────────────────

    /**
     * Flush les chunks en attente de tous les joueurs en ligne.
     * Appelé de façon asynchrone toutes les 30 secondes.
     */
    public void flushAll() {
        for (org.bukkit.entity.Player player : plugin.getServer().getOnlinePlayers()) {
            UUID uuid = player.getUniqueId();
            flush(uuid, player.getName(), player.getWorld().getName());
        }
    }

    private void flush(UUID uuid, String pseudo, String world) {
        Set<String> chunks = pending.remove(uuid);
        if (chunks == null || chunks.isEmpty()) return;

        String worldName = trackedWorlds.contains(world) ? world : "world";

        // Convertir en liste de ChunkEntry
        List<WebMapAPI.ChunkEntry> entries = new ArrayList<>(chunks.size());
        for (String key : chunks) {
            String[] parts = key.split(",");
            if (parts.length == 2) {
                try {
                    entries.add(new WebMapAPI.ChunkEntry(
                        Integer.parseInt(parts[0]),
                        Integer.parseInt(parts[1])
                    ));
                } catch (NumberFormatException ignored) {}
            }
        }

        // Batch : max 200 par requête
        int batchSize = plugin.getConfig().getInt("chunk-batch-size", 200);
        for (int i = 0; i < entries.size(); i += batchSize) {
            List<WebMapAPI.ChunkEntry> batch = entries.subList(i, Math.min(i + batchSize, entries.size()));
            api.pushChunks(uuid.toString(), pseudo, worldName, batch);
        }
    }
}
