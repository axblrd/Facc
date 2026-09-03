# 🏰 FactionSystem — Système Complet de Factions pour Minecraft

<div align="center">

![Minecraft](https://img.shields.io/badge/Minecraft-1.21-00B200?style=for-the-badge&logo=minecraft)
![Java](https://img.shields.io/badge/Java-21-F7A81D?style=for-the-badge&logo=openjdk)
![Paper](https://img.shields.io/badge/Paper-Spigot-F7A81D?style=for-the-badge)
![License](https://img.shields.io/badge/License-MIT-green?style=for-the-badge)
![Release](https://img.shields.io/github/v/release/axblrd/Facc?style=for-the-badge)

**Système complet de factions avec synchronisation web en temps réel pour serveur Paper/Spigot 1.21**

📦 **[Télécharger la dernière version](https://github.com/axblrd/Facc/releases/latest)**

</div>

---

## 🚀 Présentation

**FactionSystem** est une solution tout-en-un pour gérer un serveur Minecraft PvP/Factions. Il combine :

- 🎮 **FactionPlugin** : Gestion complète de factions avec guerre, économie et stats
- 🗺️ **FactionWebMap** : Synchronisation des territoires et données vers un site web

Que vous dirigiez un serveur compétition ou un serveur survie, FactionSystem vous offre tous les outils nécessaires pour créer une expérience de jeu captivante et structurée.

---

## 🗺️ FactionWebMap — NOUVEAU ! (v1.0.0)

**FactionWebMap** est un plugin complémentaire qui synchronise les données de factions et l'exploration des joueurs vers un site web en temps réel.

### ✨ Fonctionnalités

| Fonctionnalité | Description |
|-----------------|-------------|
| 📍 **Suivi des Chunks** | Tracker automatiquement les chunks visités par chaque joueur |
| 🏰 **Snapshot des Factions** | Envoyer un snapshot complet des données de faction toutes les 60 secondes |
| 📊 **Statistiques Joueurs** | Synchroniser les stats individuelles des joueurs |
| 🌐 **Intégration Web** | Connexion transparente avec le site web FactionSite |
| ⚙️ **Configuration Flexible** | Intervalle de push et taille des lots configurable |
| 🐛 **Mode Debug** | Option de débogage pour faciliter le dépannage |

### 🔧 Installation Rapide

```bash
# Compilation
mvn -f pom_webmap.xml clean package

# Le fichier JAR sera dans target/FactionWebMap-1.0.0.jar
```

1. Placez `FactionWebMap-1.0.0.jar` dans le dossier `plugins` de votre serveur
2. **SoftDepend** de `FactionPlugin` (optionnel mais recommandé)
3. Redémarrez le serveur
4. Modifiez `plugins/FactionWebMap/config.yml` avec l'URL de votre site et votre clé API

### ⚙️ Configuration

```yaml
site-url: "http://localhost:3000"
api-key: "votre-cle-api"
push-interval-ticks: 1200  # 60 secondes
chunk-batch-size: 200      # chunks par envoi
worlds-tracked:
  - world
debug: false
```

---

## 🎮 FactionPlugin — Plugin Principal

✨ **Version 5.8.3** : Système complet de gestion de factions avec guerre, commerce, statistiques et synchronisation web en temps réel.

### ✨ Fonctionnalités Principales

| Catégorie | Fonctionnalités |
|----------|-----------------|
| ⚔️ **Combat & PvP** | Système de guerre complet avec conquêtes territoriales, inventaire partagé, sessions de combat, mode Arène |
| 🏦 **Economie** | Banque d'émeraudes partagée, shop global multi-devises (fer, or, diamant, émeraude) |
| 🗺️ **Territoire** | Claims de chunks, permissions par joueur, visualisation sur carte |
| 📊 **Progression** | Système de puissance (PI/PG), 7 rangs (Pierre → Légendaire), effets passifs |
| 🛒 **Commerce** | Echanges sécurisés entre joueurs, boutique mondiale avec recherche et tri |
| 🏠 **Social** | Homes personnels, TPA, alliances entre factions, coffres privés |
| 🌐 **Site Web** | FactionSite v2 intégré — classements, carte interactive, profils de factions |

---

## 📦 Installation

### Prérequis
- **Java 21**
- **Maven 3.9+**
- Paper/Spigot 1.21

### Compilation

#### FactionPlugin (plugin principal)
```bash
mvn -f pom.xml clean package
# JAR: target/FactionPlugin-5.8.3.jar
```

#### FactionWebMap (synchronisation web)
```bash
mvn -f pom_webmap.xml clean package
# JAR: target/FactionWebMap-1.0.0.jar
```

### ⚔️ Système de Guerre Complet
- **Déclaration de guerre** entre factions avec système de sessions de combat
- **Conquêtes de territoires** : Les factions victorieuses gagnent les claims ennemis
- **Inventaire partagé en guerre** : Accès aux ressources communes pendant les combats
- **Gestion structurée des participants** et des sessions de guerre
- **Mode Arène** : Arènes de combat dédiées avec matchmaking et stats détaillées

### 🌐 FactionSite v2 — Interface Web (NOUVEAU !)

**FactionSite v2** est un site web moderne qui complète parfaitement le plugin !

- **🌟 Interface utilisateur moderne** : Design épuré et responsive pour une expérience optimale
- **📊 Tableaux de bord en temps réel** : Statistiques des joueurs et des factions toujours à jour
- **🗺️ Carte interactive** : Visualisez les territoires des factions directement depuis le navigateur
- **🏰 Profils de factions** : Pages détaillées avec historique, membres, et stats
- **⚔️ Mode Arène** : Participez à des combats compétitifs et consultez les classements
- **🔄 Synchronisation bidirectionnelle** : Les données circulent seamlessly entre le serveur et le site

#### Installation du Site Web
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

### 🏦 Banque de Faction (Emerald Bank)
- **Coffre partagé** : Déposez et retirez des émeraudes dans la banque de votre faction
- **Interface GUI intuitive** pour gérer les dépôts/retraits
- **Accès réservé aux membres autorisés**
- **Historique des transactions**

### 🗺️ Système de Claims
- **Réclamez des chunks** pour protéger votre territoire
- **Permissions par joueur** : Configurez qui peut construire/casser dans les zones réclamées
- **Visualisation des claims** sur la carte
- **GUI de gestion des permissions** intuitive

### 💱 Commerce Entre Joueurs (Trade)
- **Échange sécurisé** d'items entre deux joueurs
- **Interface GUI** avec slots pour proposer items et émeraudes
- **Confirmation des deux parties** requise pour finaliser l'échange
- **Protection contre les scams**

### 🛒 Shop Global (`/faction shop`)
- **GUI paginé** pour parcourir toutes les annonces
- **Recherche par mot-clé** dans le GUI
- **Tri par prix** croissant ou décroissant
- **4 devises acceptées** : Fer, Or, Diamant, Émeraude
- **Paiement instantané** au vendeur

### ⚡ Système de Puissance (Power System)
- **Puissance Individuelle (PI)** : Générée basée sur les stats PvP, survie et activité
- **Puissance Globale (PG)** : Somme des PI des membres + bonus de taille
- **7 Rangs de Faction** : Pierre → Bronze → Argent → Or → Diamant → Emeraude → Légendaire
- **Effets passifs** : Speed, force, résistance selon le rang

### 📊 Statistiques Joueurs Intégrées
- **`/faction stats [joueur]`** : Stats complètes (kills, mobs, dégâts, blocs, temps, K/D)
- **`/faction classementjoueurs`** : Top 10 par catégorie

### 🏠 Fonctionnalités de Base
- **Création de factions** : `/faction create <nom>`
- **Gestion des membres** : Invitez, expulsez, promouvez
- **Système de téléportation (TPA)** : `/tpa`, `/tpaccept`, `/tpdeny`
- **Homes personnels** : `/sethome`, `/home`, `/delhome`, `/homes`
- **Inventaire partagé** : Coffre commun accessible à tous
- **Interface GUI** : Menu complet avec `/faction`
- **Alliances** entre factions
- **Coffres privés** individuels
- **InvSee** pour les admins (`/faction invsee <joueur>`)
- **Liaison site web** : `/lier` pour connecter le compte Minecraft au site
- **Classement factions** par puissance
- **Tri de coffre** par type, rareté ou ordre alphabétique
- **Map faction** interactive

## 🔧 Installation

### 📥 Téléchargement rapide
📦 **[Télécharger FactionPlugin-5.8.3.jar](https://github.com/axblrd/Facc/releases/download/v5.8.2/FactionPlugin-5.8.3.jar)**

### Prérequis
- **Java 21**
- **Maven 3.9+**
- Paper/Spigot 1.21

### Compilation depuis les sources

```bash
git clone https://github.com/axblrd/Facc.git
cd Facc
mvn clean package
```

Le fichier JAR sera généré dans `target/FactionPlugin-5.8.3.jar`

### Installation rapide
1. Téléchargez `FactionPlugin-5.8.3.jar` depuis la [page des releases](https://github.com/axblrd/Facc/releases/latest)
2. Placez le fichier dans le dossier `plugins` de votre serveur
3. Redémarrez le serveur
4. Le fichier de configuration sera généré automatiquement dans `plugins/FactionPlugin/`

## 📡 Commandes Principales

| Commande | Description |
|----------|-------------|
| `/faction` | Menu principal |
| `/faction create <nom>` | Créer une faction |
| `/faction info` | Informations de votre faction |
| `/faction invite <joueur>` | Inviter un joueur |
| `/faction leave` | Quitter votre faction |
| `/faction stats [joueur]` | Statistiques d'un joueur |
| `/faction classementjoueurs` | Top 10 joueurs |
| `/faction shop` | Ouvrir le shop global |
| `/faction vendre <prix> <monnaie>` | Mettre un item en vente |
| `/faction shop <joueur>` | Voir l'inventaire d'un joueur (admin) |
| `/tpa <joueur>` | Demander une téléportation |
| `/tpaccept` / `/tpdeny` | Accepter/refuser un TPA |
| `/sethome [nom]` | Définir un home |
| `/home [nom]` | Se téléporter à un home |
| `/lier` | Lier le compte au site web |

## 🔑 Permissions

| Permission | Description |
|------------|-------------|
| `faction.use` | Utiliser les commandes de base (par défaut: tous) |
| `faction.admin` | Commandes admin - InvSee, bypass (par défaut: ops) |

## 🏗️ Architecture

```
fr.faction
├── FactionPlugin.java          # Classe principale
├── alliance/                   # Système d'alliance et homes
├── claim/                      # Gestion des claims et permissions
├── commands/                   # Commandes (/faction, /power)
├── economy/                    # Banque d'émeraudes
├── gui/                        # Interfaces GUI
├── listeners/                  # Event listeners
├── managers/                   # Gestionnaires (faction, stats, etc.)
├── map/                       # Carte des factions
├── models/                    # Modèles de données
├── power/                     # Système de puissance
├── shop/                      # Boutique globale
├── sort/                      # Tri de coffre
├── trade/                     # Commerce entre joueurs
├── war/                       # Système de guerre
├── web/                       # Liaison site web
└── util/                      # Utilitaires
```

## 📜 Historique des Versions

### v5.8.3 — Améliorations & Optimisations ⚡

> **Version d'optimisation** avec améliorations de performance et nouvelles fonctionnalités pour une meilleure expérience de jeu !

#### 🚀 Améliorations de Performance
- **Optimisation du système de claims** : Réduction significative de la latence lors de la vérification des permissions de territoire
- **Cache amélioré** pour les classements de factions et joueurs
- **Amélioration de la gestion des events** : Traitement plus efficace des événements joueurs

#### 🔧 Nouvelles Fonctionnalités
- **Notification de raid** : Alertes automatiques quand une faction adverse entre dans votre territoire
- **Historique des transactions** : Journal complet des échanges et ventes dans le shop
- **Commandes de modération enrichies** : Nouvelles options pour les admins

#### 🛡️ Sécurité & Stabilité
- **Validation des entrées** renforcée pour toutes les commandes
- **Protection anti-exploit** améliorée sur le système de trade
- **Gestion des connexions concurrentes** optimisée

#### 🐛 Corrections
- Correction d'un bug critique sur la synchronisation des homes de faction
- Résolution de problèmes de latence lors des guerres de factions
- Correction de l'affichage des stats pour les joueurs hors-ligne

### v5.8.2 — Fusion FactionPlugin + FactionStats & Améliorations ✨
> **Version de fusion** avec intégration complète des statistiques avancées !

#### 🔗 Fusion avec FactionStats
- **Commande enrichie `/faction stats [joueur]`** : Ajout des mobs hostiles tués, dégâts reçus, dates de première/dernière connexion, classements affichés en ligne
- **Fonctionne pour les joueurs hors ligne** : Plus besoin d'être connecté pour consulter les stats
- **`/faction classementjoueurs <categorie>`** : Top 10 joueurs par catégorie (`mobs`, `pvp`, `advancements`, `morts`, `blocs`, `temps`, `dommages`, `kd`)

#### ⚡ Système de Puissance Amélioré
- **Tracking mobs hostiles** : Ajout du compteur `mobsKilled`
- **Dégâts reçus trackés** : Y compris hors combat direct (chute, feu, noyade...)
- **Temps de jeu précis** : Tâche périodique (+20 ticks/seconde) — plus robuste en cas de crash serveur

#### 🛠️ Améliorations Techniques
- Nouveau fichier `StatsMessageUtil` : formatage des nombres, séparateurs, médailles de classement
- Fichier `stats.yml` rétrocompatible : Les anciens fichiers se rechargent sans erreur

#### 🐛 Corrections
- Corrections de bugs TPA/Home
- Résolution de problèmes de visualisation claims
- Amélioration des performances GUI

### v5.8.0 — Site Web FactionSite v2 & Améliorations Majeures ✨
> **Version majeure** avec intégration complète du site web FactionSite v2 !

#### 🌐 FactionSite v2 — NOUVEAU
- **Interface web moderne** : Design responsive et attractif
- **Classements en temps réel** : Statistiques joueur et faction toujours à jour
- **Carte interactive** : Visualisez les territoires des factions sur le web
- **Profils de factions** : Pages détaillées avec historique et membres
- **Mode Arène** : Système de combat compétitif avec matchmaking
- **Synchronisation bidirectionnelle** : Données en temps réel entre serveur et site

#### ⚔️ Mode Arène
- Arènes de combat dédiées
- Système de matchmaking
- Statistiques de session détaillées

#### 🔧 Améliorations Techniques
- Optimisation des requêtes base de données
- Mise en cache améliorée pour les classements
- Sécurité renforcée
- Stabilité générale améliorée

#### 🐛 Corrections
- Corrections de bugs TPA/Home
- Résolution de problèmes de visualisation claims
- Amélioration des performances GUI

### v5.7.2 — Correction des Bugs

### Versions précédentes
- **v5.5.0** : Système de guerre, tri de coffre, GUI principal + corrections
- **v5.4.1** : Optimisations et corrections de bugs
- **v5.4.0** : Shop Global paginé + InvSee admin
- **v4.0.0** : Refonte complète de la boutique + InvSee
- v3.2.4 : Claim, Banque émeraudes, Troc, Stats, Classements, Puissance
- v3.1.0 : Stats joueurs intégrées + Classement
- v2.0.0 : Système de Puissance et Rangs
- v1.0.0 : Version initiale

## 🔗 Intégration Web

Ce plugin est conçu pour fonctionner avec **[FactionWebMap](https://github.com/axblrd/Facc)** — un plugin complémentaire qui synchronise les données de factions et l'exploration des joueurs vers un site web en temps réel.

## 📄 License

Ce projet est sous licence MIT.

---

⭐ N'hésitez pas à laisser une étoile si ce plugin vous est utile !