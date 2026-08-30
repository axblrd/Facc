# FactionWebMap

Plugin Minecraft pour **Paper 1.21** qui synchronise les données de factions et l'exploration des joueurs vers un site web en temps réel.

## 🎯 Description

**FactionWebMap** est un pont entre votre serveur Minecraft et votre site web de faction. Il permet de :

- 📍 **Tracker l'exploration** des joueurs en temps réel (chunks visités)
- 🏰 **Synchroniser les données de factions** : membres, alliés, puissance, ranks
- 🗺️ **Envoyer les claims** et les positions des homes vers le site web
- 📊 **Transmettre les statistiques** des joueurs (kills, deaths, playtime)

Ce plugin fonctionne en conjonction avec [FactionPlugin](https://github.com/axblrd/Facc) pour créer une expérience web immersive où les joueurs peuvent visualiser leur progression et celle de leur faction.

## ✨ Fonctionnalités

### 🗺️ Tracking des Chunks Exploré
- Détecte automatiquement les chunks visités par chaque joueur
- Envoie les données par lots (configurable, défaut: 200 chunks/requête)
- Support multi-mondes (configurable)
- Flush automatique toutes les 30 secondes + flush à la déconnexion
- Déduplication côté serveur (INSERT IGNORE)

### 🏰 Synchronisation des Factions
- **Snapshot global** envoyé périodiquement (défaut: 60 secondes)
- Inclut :
  - Informations complètes des factions (nom, chef, rang, puissance)
  - Liste des membres et alliés
  - Position du spawn de faction
  - Barycentre des claims (centre du territoire)
  - Tous les claims avec leurs coordonnées

### 🏠 Homes de Faction
- Synchronisation des points de téléportation des joueurs
- Position en coordonnées de chunk pour optimisation web

### 📊 Statistiques Joueurs
- Transmission des stats en temps réel :
  - Faction actuelle
  - Puissance individuelle
  - Kills / Deaths
  - Temps de jeu

### 🔒 Sécurité & Performance
- Communication via API REST sécurisée (clé API)
- Requêtes HTTP asynchrones (n'impacte pas le serveur)
- Connexion timeout 5s, requête timeout 8s
- Mode debug pour troubleshooting

## 🔧 Installation

### Prérequis
- **Java 21**
- **Maven 3.9+**
- Paper 1.21 ou compatible
- Un site web avec l'API appropriée

### Compilation

```bash
mvn clean package
```

Le fichier JAR sera généré dans `target/FactionWebMap-1.1.0.jar`

### Configuration

1. Placez `FactionWebMap-1.1.0.jar` dans le dossier `plugins` de votre serveur
2. Redémarrez le serveur
3. Modifiez `plugins/FactionWebMap/config.yml` :

```yaml
# URL de votre site web
site-url: "https://votre-site.com"

# Clé API (doit correspondre à FACTION_API_KEY dans .env du site)
api-key: "votre-cle-secrete"

# Intervalle d'envoi des snapshots (en ticks, 1200 = 60 secondes)
push-interval-ticks: 1200

# Nombre max de chunks par requête
chunk-batch-size: 200

# Worlds à tracker
worlds-tracked:
  - world

# Mode debug (affiche les requêtes dans la console)
debug: false
```

## 🔌 Dépendance avec FactionPlugin

Ce plugin est conçu pour fonctionner **avec** [FactionPlugin](https://github.com/axblrd/Facc), mais peut tourner **seul** :

| Scénario | Comportement |
|----------|--------------|
| **FactionPlugin installé** | ✅ Toutes les fonctionnalités actives |
| **FactionPlugin non installé** | ⚠️ Snapshot désactivé, chunks & stats envoyés |

## 📡 API Web

Le plugin communique avec les endpoints suivants :

| Endpoint | Méthode | Description |
|----------|---------|-------------|
| `/api/faction/push/chunks` | POST | Envoyer les chunks explorés |
| `/api/faction/push/snapshot` | POST | Envoyer le snapshot des factions |
| `/api/faction/push/stats` | POST | Envoyer les stats des joueurs |

**Headers requis :**
- `Content-Type: application/json`
- `X-Faction-Key: <api-key>`

## 🛠️ Développement

### Architecture

```
FactionWebMapPlugin
├── WebMapAPI          # Client HTTP léger (java.net.http)
├── ChunkTracker       # Listener des événements de mouvement
└── SnapshotPusher     # Collecte et envoie les données
```

### API Endpoints Attendus

Le site web doit implémenter ces endpoints pour recevoir les données :

```javascript
// POST /api/faction/push/chunks
{
  "uuid": "player-uuid",
  "pseudo": "PlayerName",
  "world": "world",
  "chunks": [{"cx": 100, "cz": -50}, ...]
}

// POST /api/faction/push/snapshot
{
  "factions": [
    {
      "name": "FactionName",
      "chef": "chef-uuid",
      "rank": "Diamant",
      "power": 1500.5,
      "members": ["uuid1", "uuid2"],
      "allies": ["AlliedFaction"],
      "spawnCx": 100, "spawnCz": -50,
      "centerCx": 120, "centerCz": -45
    }
  ],
  "claims": [
    {"faction": "FactionName", "cx": 100, "cz": -50, "world": "world"}
  ],
  "homes": [
    {"uuid": "uuid", "name": "home1", "cx": 100, "cz": -50, "world": "world"}
  ]
}

// POST /api/faction/push/stats
{
  "uuid": "player-uuid",
  "pseudo": "PlayerName",
  "faction": "FactionName",
  "power": 25.5,
  "kills": 42,
  "deaths": 15,
  "playtimeMs": 7200000
}
```

## 📜 Historique des Versions

- **v1.1.0** : Amélioration du système de snapshot et optimisations
- **v1.0.0** : Version initiale - Tracking des chunks, snapshot factions, stats joueurs

## 📄 License

Ce projet est sous licence MIT.