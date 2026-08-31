# 🏰 FactionPlugin

Plugin Minecraft complet de gestion de factions pour serveur **Paper/Spigot 1.21**.

> Plugin tout-en-un combinant gestion de factions, statistiques joueurs, système de puissance, shop global, banque d'émeraudes, système de claims, commerce, guerre et bien plus encore !

## ✨ Fonctionnalités

### ⚔️ Système de Guerre Complet
- **Déclaration de guerre** entre factions avec système de sessions de combat
- **Conquêtes de territoires** : Les factions victorieuses gagnent les claims ennemis
- **Inventaire partagé en guerre** : Accès aux ressources communes pendant les combats
- **Gestion structurée des participants** et des sessions de guerre

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

### Prérequis
- **Java 21**
- **Maven 3.9+**
- Paper/Spigot 1.21

### Compilation

```bash
git clone https://github.com/axblrd/Facc.git
cd Facc
mvn clean package
```

Le fichier JAR sera généré dans `target/FactionPlugin-5.7.2.jar`

### Installation rapide
1. Téléchargez la dernière version depuis la [page des releases](../../releases)
2. Placez `FactionPlugin-X.X.X.jar` dans le dossier `plugins` de votre serveur
3. Redémarrez votre serveur
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

### v5.7.2
- Système de guerre complet avec conquêtes territoriales
- Inventaire partagé pendant les combats
- Tri de coffre avancé (type, rareté, alphabétique)
- GUI principal redesigné
- Commandes TPA améliorées avec cooldowns
- Commandes Home enrichies avec auto-completion
- Multiples corrections et optimisations

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