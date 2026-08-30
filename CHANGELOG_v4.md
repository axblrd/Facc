# CHANGELOG — FactionPlugin

---

## v5.5.0

### ⚔️ Système de Guerre Complet
- **Déclaration de guerre** entre factions avec système de sessions de combat
- **Conquêtes de territoires** : Les factions victorieuses peuvent gagner les claims ennemis
- **Inventaire partagé en guerre** : Accès aux ressources communes pendant les combats
- **Sessions de guerre structurées** avec gestion des participants

### 📦 Tri de Coffre (Chest Sorting)
- **Menu GUI** pour choisir le mode de tri
- **Tri par type** : Organise les objets par catégorie (blocs, outils, armes, etc.)
- **Tri par rarity** : Classe les objets selon leur rareté
- **Tri alphabétique** : Ordonne les items par nom
- **Tri du coffre partagé** de la faction

### 🏠 GUI Principal Refait
- **Menu principal redesigné** avec une interface plus intuitive
- **Accès rapide** à toutes les fonctionnalités de la faction
- **Affichage des stats** et informations importantes
- **Navigation améliorée** entre les différentes sections

### 🔧 Commandes TPA Améliorées
- `/tpa <joueur>` : Demander une téléportation
- `/tpaccept` : Accepter une demande
- `/tpdeny` : Refuser une demande
- **Cooldowns** et **limites** configurables

### 🔧 Commandes Home Améliorées
- `/sethome [nom]` : Définir un point de téléportation
- `/home [nom]` : Se téléporter à un home
- `/delhome <nom>` : Supprimer un home
- `/homes` : Lister tous ses homes
- **Auto-completion** des noms de homes
- **Limites de homes** selon le statut (solo, faction, alliance)

### 🐛 Corrections et Améliorations
- Corrections de bugs liés aux téléportations
- Optimisation des performances globales
- Amélioration de la gestion des events
- Refactorisation du code pour une meilleure maintenabilité

---

## v5.4.1

### 🐛 Corrections et Améliorations
- **Optimisation du Shop** : Amélioration des performances de recherche et de pagination
- **Corrections de bugs** : Résolution de problèmes de performance dans la gestion des inventaires
- **Améliorations GUI** : Affichage plus fluide des interfaces utilisateur
- **Optimisation mémoire** : Réduction de l'empreinte mémoire du plugin
- **Corrections de bugs** liés aux téléportations et aux homes

### 🔧 Améliorations Techniques
- Refactorisation du code pour une meilleure maintenabilité
- Optimisation des requêtes de base de données
- Amélioration de la gestion des événements asynchrones

---

## v5.4.0

### 🛒 Shop Global (`/faction shop`)
- GUI paginé (5 rangées × 9 = 45 items/page)
- Recherche par mot-clé : clic sur le panneau dans le GUI, puis saisie dans le chat
- Tri par prix croissant (`↑`) ou décroissant (`↓`)
- Monnaies acceptées : Lingot de fer, Lingot d'or, Diamant, Émeraude
- Paiement automatique au vendeur dès la vente (ou livré à la reconnexion si hors-ligne)
- Drop à tes pieds si l'inventaire est plein (acheteur ET vendeur)
- Vue "Mes annonces" depuis le GUI ou `/faction mesannonces`

### Commandes shop
| Commande | Description |
|---|---|
| `/faction shop` | Ouvrir le shop (GUI) |
| `/faction vendre <prix> <monnaie>` | Mettre l'item en main en vente |
| `/faction acheter <ID>` | Acheter directement par ID |
| `/faction recuperer [ID]` | Récupérer une annonce non vendue (sans ID = liste) |
| `/faction mesannonces` | Voir ses annonces (GUI) |

Monnaies : `fer`, `or`, `diamant`, `emeraude`

### 👁️ InvSee (`/faction invsee <joueur>`)
- Permission requise : `faction.admin`
- Affiche l'inventaire complet du joueur (36 slots + hotbar + armure + offhand)
- Lecture seule : aucun item ne peut être pris ou déplacé
- Message d'état dans le chat (joueur ciblé + confirmation lecture seule)

---

## v4.0.0

### 🛒 Shop Global (`/faction shop`)
- GUI paginé (5 rangées × 9 = 45 items/page)
- Recherche par mot-clé : clic sur le panneau dans le GUI, puis saisie dans le chat
- Tri par prix croissant (`↑`) ou décroissant (`↓`)
- Monnaies acceptées : Lingot de fer, Lingot d'or, Diamant, Émeraude
- Paiement automatique au vendeur dès la vente (ou livré à la reconnexion si hors-ligne)
- Drop à tes pieds si l'inventaire est plein (acheteur ET vendeur)
- Vue "Mes annonces" depuis le GUI ou `/faction mesannonces`

### Commandes shop
| Commande | Description |
|---|---|
| `/faction shop` | Ouvrir le shop (GUI) |
| `/faction vendre <prix> <monnaie>` | Mettre l'item en main en vente |
| `/faction acheter <ID>` | Acheter directement par ID |
| `/faction recuperer [ID]` | Récupérer une annonce non vendue (sans ID = liste) |
| `/faction mesannonces` | Voir ses annonces (GUI) |

Monnaies : `fer`, `or`, `diamant`, `emeraude`

### 👁️ InvSee (`/faction invsee <joueur>`)
- Permission requise : `faction.admin`
- Affiche l'inventaire complet du joueur (36 slots + hotbar + armure + offhand)
- Lecture seule : aucun item ne peut être pris ou déplacé
- Message d'état dans le chat (joueur ciblé + confirmation lecture seule)

## Fichiers ajoutés
```
src/main/java/fr/faction/shop/
  ├── ShopListing.java     ← Modèle d'annonce
  ├── ShopManager.java     ← Logique métier + persistance (shop.yml)
  ├── ShopGUI.java         ← GUI paginé avec recherche + Listener
  └── InvSeeGUI.java       ← GUI InvSee admin + Listener
```

## Modifications
- `FactionPlugin.java` : intégration des nouveaux managers
- `FactionCommand.java` : +8 nouvelles sous-commandes
- `PlayerListener.java` : hook recherche chat + livraison paiements en attente
- `plugin.yml` : version 4.0.0

## Fichier de données
`plugins/FactionPlugin/shop.yml` — auto-créé au premier `/faction vendre`

---

## Historique
- **v5.5.0** : Système de guerre, tri de coffre, GUI principal + corrections
- **v5.4.1** : Optimisations et corrections de bugs
- **v5.4.0** : Shop Global paginé + InvSee admin
- **v4.0.0** : Refonte complète de la boutique + InvSee
- v3.2.4 : Claim, Banque émeraudes, Troc, Stats, Classements, Puissance
- v3.2.3 : Améliorations utilitaires
- v3.2.2 : Optimisations système de puissance
- v3.2.1 : Corrections système de Trade
- v3.2.0 : Banque d'émeraudes, Système de Claims et Commerce
- v3.1.0 : Stats joueurs intégrées + Classement
- v2.0.0 : Système de Puissance et Rangs
- v1.0.0 : Version initiale
