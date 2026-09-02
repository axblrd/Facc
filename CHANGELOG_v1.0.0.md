# 📋 CHANGELOG — FactionSystem v1.0.0

## 🆕 FactionWebMap — Nouvelle Extension pour FactionSystem

> **Grande nouvelle!** FactionSystem s'enrichit d'un nouveau plugin : **FactionWebMap v1.0.0** ! Cette extension permet de synchroniser les données de votre serveur Minecraft avec un site web en temps réel.

---

## 🗺️ FactionWebMap v1.0.0 — Fonctionnalités

### 📍 Suivi des Chunks en Temps Réel
- **ChunkTracker** : Système de tracking automatique des chunks visités par chaque joueur
- **Flush périodique** : Envoi des données toutes les 30 secondes
- **Traitement par lots** : Envoi optimisé de 200 chunks maximum par push
- **Multi-mondes** : Support configurable pour plusieurs mondes

### 🏰 Synchronisation des Données de Faction
- **SnapshotPusher** : Envoi automatique des snapshots de faction
- **Données synchronisées** :
  - Liste des factions avec leurs membres
  - Territoires et claims
  - Puissance et rangs
  - Statistiques des joueurs
- **Intervalle configurable** : Par défaut toutes les 60 secondes

### 🌐 API Web Intégrée
- **WebMapAPI** : Communication REST avec le site web
- **Authentification** : Clé API pour sécuriser les communications
- **Mode debug** : Journalisation détaillée pour le dépannage
- **Gestion des erreurs** : Retry automatique en cas d'échec

### ⚙️ Configuration Flexible
```yaml
site-url: "http://localhost:3000"    # URL du site web
api-key: "change-moi"                # Clé API (à configurer)
push-interval-ticks: 1200           # 60 secondes par défaut
chunk-batch-size: 200               # 200 chunks par lot
worlds-tracked:                     # Mondes à tracker
  - world
debug: false                         # Mode debug
```

### 🔧 Architecture Technique
- **Standalone ou addon** : Fonctionne seul ou avec FactionPlugin
- **SoftDepend** : Dépendance optionnelle vers FactionPlugin
- **Shaded JAR** : Gson embarqué, aucune dépendance externe
- **Paper 1.21+** : Compatible avec la dernière version de Paper

---

## 📦 Contenu de la Release

| Fichier | Description |
|---------|-------------|
| `FactionWebMap-1.0.0.jar` | Plugin de synchronisation web |
| `pom_webmap.xml` | Configuration Maven pour compilation |
| `src_webmap/` | Code source du plugin |

---

## 🔗 Intégration avec FactionSite

FactionWebMap est conçu pour fonctionner avec **FactionSite v2** (inclus dans FactionSystem). Ensemble, ils offrent :

- 🗺️ **Carte Interactive** : Visualisez les territoires des factions en temps réel
- 📊 **Tableaux de Bord** : Statistiques complètes des joueurs et factions
- 🏰 **Profils de Factions** : Pages détaillées avec historique et membres
- ⚔️ **Mode Arène** : Système de combat compétitif

### Installation de FactionSite

```bash
# Extraire le fichier FactionSite-v2.zip
unzip FactionSite-v2.zip
cd mc-site-faction

# Installer les dépendances
npm install

# Configurer (.env)
cp .env.example .env
# Modifier les paramètres selon votre serveur

# Lancer le serveur
npm start
```

---

## 🚀 Installation Rapide de FactionWebMap

1. **Téléchargez** `FactionWebMap-1.0.0.jar` depuis la [page des releases](../../releases)
2. **Placez** le fichier dans le dossier `plugins` de votre serveur Paper/Spigot
3. **Redémarrez** votre serveur
4. **Configurez** `plugins/FactionWebMap/config.yml` avec :
   - L'URL de votre site web (site-url)
   - Votre clé API secrète (api-key)

### Compilation Manuelle

```bash
# Cloner le dépôt
git clone https://github.com/axblrd/Facc.git
cd Facc

# Compiler FactionWebMap
mvn -f pom_webmap.xml clean package

# Le JAR sera dans target/FactionWebMap-1.0.0.jar
```

---

## ⚠️ Notes Importantes

- **Java 21 requis** : Cette version nécessite Java 21 pour fonctionner
- **Paper 1.21+** : Compatible uniquement avec Paper ou Spigot 1.21 et supérieur
- **Optionnel** : FactionWebMap peut fonctionner seul ou avec FactionPlugin
- **FactionSite requis** : Pour utiliser la synchronisation web, installez aussi FactionSite

---

## 🔮 Prochaines Versions

Les fonctionnalités prévues pour les prochaines versions :

- Support WebSocket pour des mises à jour en temps réel
- Interface d'administration dans le jeu
- Dashboard de monitoring intégré
- Export des données en JSON/CSV

---

## 📨 Rapports de Bugs & Suggestions

Si vous trouvez un bug ou avez une suggestion, n'hésitez pas à :
- Ouvrir une issue sur [GitHub](https://github.com/axblrd/Facc/issues)
- Soumettre une pull request

---

**Amusez-vous bien sur votre serveur Factions ! 🏰⚔️🗺️**
