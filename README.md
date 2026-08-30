# FactionPlugin

Plugin Minecraft complet de gestion de factions pour serveur **Paper 1.21**

## Description

**FactionPlugin** est un plugin Minecraft tout-en-un qui révolutionne l'expérience de jeu sur votre serveur ! Il combine gestion de factions avancée, statistiques détaillées, système de puissance stratégique, commerce entre joueurs, et bien plus encore.

Conçu pour Paper 1.21, ce plugin offre une expérience immersive et complète avec une interface intuitive via GUI, des systèmes de protection de territoire, un écosystème économique intégré, un système de guerre entre factions, et bien d'autres fonctionnalités premium.

## ✨ Fonctionnalités Principales

### 🏰 Système de Factions Complet
- **Création et gestion de factions** avec rôles hiérarchiques (Chef, Officier, Membre)
- **Interface GUI interactive** accessible via `/faction`
- **Invitations et gestion des membres** en temps réel
- **Système d'alliances** entre factions pour des alliances stratégiques

### ⚔️ Système de Guerre (War System)
- **Déclarez la guerre** à d'autres factions ennemies
- **Système de sessions de combat** structuré
- **Conquêtes de territoires** : Gagnez les claims ennemis
- **Récompenses de victoire** pour les factions victorieuses

### 🏦 Banque de Faction (Emerald Bank)
- **Coffre partagé** : Déposez et retirez des émeraudes en toute sécurité
- **Interface GUI intuitive** pour gérer les dépôts/retraits
- **Accès contrôlé** : Configurez qui peut accéder à la banque
- **Historique des transactions** pour une transparence totale

### 🗺️ Système de Claims
- **Réclamez des chunks** pour votre faction et marquez votre territoire
- **Visualisation des claims** en temps réel
- **Permissions granulares** : Configurez précisément qui peut construire/casser
- **Protection avancée** : Interdiction d'accès aux non-membres

### 💱 Commerce Entre Joueurs (Trade)
- **Échanges sécurisés** avec validation des deux parties
- **Interface GUI moderne** pour proposer items et émeraudes
- **Protection anti-scam** intégrée
- **Annulation possible** à tout moment

### 🛒 Shop Global (`/faction shop`)
- **Boutique communautaire** paginée et intuitive
- **Recherche par mot-clé** pour trouver rapidement vos articles
- **Tri par prix** (croissant/décroissant)
- **Multi-devises** : Fer, Or, Diamant, Émeraude
- **Paiement automatique** au vendeur

### 📦 Tri de Coffre (Chest Sorting)
- **Organisez automatiquement** vos coffres partagés
- **Menu GUI** pour choisir le mode de tri
- **Tri par type, rarity, ou ordre alphabétique**

### ⚡ Système de Puissance (Power System)
- **Puissance Individuelle (PI)** basée sur vos performances PvP, survie et activité
- **Puissance Globale (PG)** combinant les stats de tous les membres
- **7 Rangs de Faction** : Pierre → Bronze → Argent → Or → Diamant → Émeraude → Légendaire
- **Effets passifs** qui évoluent avec le rang (speed, force, résistance...)

### 📊 Statistiques Joueurs Avancées
- **Stats complètes** : Kills, morts, mobs, dégâts, blocs, temps de jeu
- **K/D Ratio** calculé automatiquement
- **Classements** des top 10 par catégorie
- **Suivi du temps de jeu** en temps réel

### 🔧 Fonctionnalités Additionnelles
- **Système de Homes** : Créez des points de téléportation (/sethome, /home, /delhome)
- **TPA** : Demandez à être téléporté vers un autre joueur (/tpa, /tpaccept, /tpdeny)
- **Coffres Privés** : Protégez vos objets personnels
- **InvSee** : Consultez l'inventaire des joueurs (admin)
- **Homes d'Alliance** : TP vers les homes des alliés

## 📋 Installation

1. Téléchargez la dernière version depuis la [page des releases](../../releases)
2. Placez le fichier `FactionPlugin-5.5.0.jar` dans le dossier `plugins` de votre serveur
3. Redémarrez votre serveur
4. Le fichier de configuration sera généré automatiquement dans `plugins/FactionPlugin/`

## ⚙️ Configuration

Le fichier `config.yml` vous permet de personnaliser :
- Les messages du plugin
- Les permissions par rôle
- Les paramètres de faction (coûts, limites, etc.)
- Les effets des rangs de puissance

## 🎮 Commandes Principales

| Commande | Description |
|----------|-------------|
| `/faction` | Ouvrir le menu principal |
| `/faction create <nom>` | Créer une nouvelle faction |
| `/faction info` | Voir les informations de votre faction |
| `/faction invite <joueur>` | Inviter un joueur |
| `/faction leave` | Quitter votre faction |
| `/faction shop` | Ouvrir la boutique globale |
| `/faction stats [joueur]` | Voir les statistiques |
| `/faction classementjoueurs` | Classement des joueurs |
| `/sethome [nom]` | Définir un point de téléportation |
| `/home [nom]` | Se téléporter à un home |
| `/delhome <nom>` | Supprimer un home |
| `/tpa <joueur>` | Demander une téléportation |
| `/tpaccept` | Accepter une demande de TP |
| `/tpdeny` | Refuser une demande de TP |

## 🔐 Permissions

| Permission | Description |
|------------|-------------|
| `faction.use` | Utiliser les commandes de base |
| `faction.admin` | Accès aux commandes admin (InvSee, bypass) |

## 🛠️ Développement

### Prérequis
- **Java 21**
- **Maven 3.9+**

### Compilation

```bash
mvn clean package
```

Le JAR compilé sera généré dans `target/FactionPlugin-5.5.0.jar`

## 📥 Télécharger

📦 **[Télécharger FactionPlugin-5.5.0.jar](../../releases/download/v5.5.0/FactionPlugin-5.5.0.jar)**

## 📜 Historique des Versions

- **v5.5.0** : Système de guerre complet, tri de coffre, GUI principal refait + corrections et optimisations
- **v5.4.1** : Améliorations et optimisations de la boutique + corrections de bugs
- **v5.4.0** : Nouvelles fonctionnalités de shop et InvSee admin
- **v4.0.0** : Shop Global paginé + InvSee admin
- **v3.2.4** : Corrections et améliorations finales
- **v3.2.3** : Améliorations utilitaires
- **v3.2.2** : Optimisations système de puissance
- **v3.2.1** : Corrections système de Trade
- **v3.2.0** : Banque d'émeraudes, Système de Claims et Commerce
- **v3.1.0** : Stats joueurs intégrées + Classement
- **v2.0.0** : Système de Puissance et Rangs
- **v1.0.0** : Version initiale

## 📄 License

Ce projet est sous licence MIT.