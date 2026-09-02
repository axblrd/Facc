package fr.faction.webmap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Envoie la position des joueurs en ligne toutes les 2 secondes.
 * Throttlé : pas d'envoi si le joueur n'a pas bougé de plus d'un bloc.
 */
public class PositionTracker implements Listener {

    private final FactionWebMapPlugin plugin;
    private final WebMapAPI api;

    // UUID → [lastX, lastZ] — pour détecter si le joueur a bougé
    private final Map<UUID, double[]> lastPos = new ConcurrentHashMap<>();

    public PositionTracker(FactionWebMapPlugin plugin, WebMapAPI api) {
        this.plugin = plugin;
        this.api    = api;

        // Envoyer les positions toutes les 2 secondes (40 ticks)
        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, this::pushAll, 40L, 40L);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onMove(PlayerMoveEvent e) {
        // Stocker seulement si le joueur a bougé (pas simple rotation)
        if (e.getFrom().getBlockX() == e.getTo().getBlockX()
                && e.getFrom().getBlockZ() == e.getTo().getBlockZ()) return;
        lastPos.put(e.getPlayer().getUniqueId(),
                new double[]{e.getTo().getX(), e.getTo().getY(), e.getTo().getZ()});
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        UUID uuid = e.getPlayer().getUniqueId();
        // Envoyer position null pour signaler la déconnexion
        api.pushPosition(uuid.toString(), e.getPlayer().getName(), null, null, null, false);
        lastPos.remove(uuid);
    }

    private void pushAll() {
        List<Map<String, Object>> positions = new ArrayList<>();
        for (Player player : Bukkit.getOnlinePlayers()) {
            UUID uuid = player.getUniqueId();
            double[] pos = lastPos.getOrDefault(uuid,
                    new double[]{player.getLocation().getX(),
                                 player.getLocation().getY(),
                                 player.getLocation().getZ()});
            Map<String, Object> p = new HashMap<>();
            p.put("uuid",    uuid.toString());
            p.put("pseudo",  player.getName());
            p.put("x",       pos[0]);
            p.put("y",       pos[1]);
            p.put("z",       pos[2]);
            p.put("world",   player.getWorld().getName());
            p.put("online",  true);
            positions.add(p);
        }
        if (!positions.isEmpty()) api.pushPositions(positions);
    }
}
