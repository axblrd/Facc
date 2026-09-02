package fr.faction.webmap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Client HTTP léger vers l'API du site web.
 * Utilise java.net.http (Java 11+) — pas de dépendance externe.
 */
public class WebMapAPI {

    private final String baseUrl;
    private final String apiKey;
    private final boolean debug;
    private final Logger log;

    private final HttpClient http = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .build();
    private static final Gson GSON = new GsonBuilder().create();

    public WebMapAPI(String baseUrl, String apiKey, boolean debug, Logger log) {
        this.baseUrl = baseUrl.replaceAll("/$", "");
        this.apiKey  = apiKey;
        this.debug   = debug;
        this.log     = log;
    }

    // ── Chunks explorés ──────────────────────────────────────────────────────────

    public record ChunkEntry(int cx, int cz, String biome) {}

    /**
     * Envoie un lot de chunks explorés pour un joueur.
     * Retourne true si le serveur a accepté (2xx).
     */
    public boolean pushChunks(String uuid, String pseudo, String world, List<ChunkEntry> chunks) {
        if (chunks.isEmpty()) return true;
        Map<String, Object> body = Map.of(
            "uuid",   uuid,
            "pseudo", pseudo,
            "world",  world,
            "chunks", chunks
        );
        return post("/api/faction/push/chunks", body);
    }

    // ── Snapshot faction ──────────────────────────────────────────────────────────

    /**
     * Envoie le snapshot global des factions.
     * @param payload Map contenant { factions, claims, alliances }
     */
    public boolean pushSnapshot(Map<String, Object> payload) {
        return post("/api/faction/push/snapshot", payload);
    }

    // ── Stats joueur ──────────────────────────────────────────────────────────────

    public boolean pushStats(String uuid, String pseudo, Map<String, Object> stats) {
        Map<String, Object> body = new java.util.HashMap<>(stats);
        body.put("uuid",   uuid);
        body.put("pseudo", pseudo);
        return post("/api/faction/push/stats", body);
    }

    // ── HTTP ─────────────────────────────────────────────────────────────────────

    private boolean post(String path, Object body) {
        try {
            String json = GSON.toJson(body);
            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + path))
                    .header("Content-Type", "application/json")
                    .header("X-Faction-Key", apiKey)
                    .timeout(Duration.ofSeconds(8))
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> resp = http.send(req, HttpResponse.BodyHandlers.ofString());
            boolean ok = resp.statusCode() >= 200 && resp.statusCode() < 300;

            if (debug || !ok) {
                log.info("[WebMap] POST " + path + " → " + resp.statusCode()
                        + (ok ? " ✔" : " ✘ " + resp.body()));
            }
            return ok;
        } catch (Exception e) {
            if (debug) log.warning("[WebMap] Erreur POST " + path + " : " + e.getMessage());
            return false;
        }
    }
}
