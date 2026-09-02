package fr.faction.webmap;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * FactionWebMapPlugin — plugin standalone ou addon au FactionPlugin existant.
 *
 * Fonctionnalités :
 *  1. Tracker les chunks visités par chaque joueur et les envoyer au site web
 *  2. Envoyer un snapshot des données de faction toutes les 60 secondes
 *  3. Envoyer les stats individuelles des joueurs
 *
 * Configuration (config.yml) :
 *  site-url: http://localhost:3000
 *  api-key: ta-cle-secrete          (même que FACTION_API_KEY dans .env)
 *  push-interval-ticks: 1200        (60s = 1200 ticks)
 *  chunk-batch-size: 200            (max chunks envoyés par push)
 *  debug: false
 */
public class FactionWebMapPlugin extends JavaPlugin {

    private WebMapAPI api;
    private ChunkTracker chunkTracker;
    private SnapshotPusher snapshotPusher;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        String siteUrl = getConfig().getString("site-url", "http://localhost:3000");
        String apiKey  = getConfig().getString("api-key",  "change-moi");
        boolean debug  = getConfig().getBoolean("debug", false);

        api             = new WebMapAPI(siteUrl, apiKey, debug, getLogger());
        chunkTracker    = new ChunkTracker(this, api);
        snapshotPusher  = new SnapshotPusher(this, api);

        // Enregistrer le listener de chunks
        getServer().getPluginManager().registerEvents(chunkTracker, this);

        // Tâche snapshot + stats toutes les N ticks (défaut 1200 = 60s)
        int interval = getConfig().getInt("push-interval-ticks", 1200);
        getServer().getScheduler().runTaskTimerAsynchronously(
            this, snapshotPusher::pushAll, 100L, interval);

        // Flush des chunks toutes les 30s
        getServer().getScheduler().runTaskTimerAsynchronously(
            this, chunkTracker::flushAll, 200L, 600L);

        getLogger().info("FactionWebMapPlugin activé — site: " + siteUrl);
    }

    @Override
    public void onDisable() {
        // Flush final des chunks avant l'arrêt
        if (chunkTracker != null) chunkTracker.flushAll();
        getLogger().info("FactionWebMapPlugin désactivé.");
    }

    public WebMapAPI getApi() { return api; }
}
