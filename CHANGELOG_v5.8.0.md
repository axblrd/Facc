# 📋 CHANGELOG — FactionPlugin v5.8.0

## 🆕 Nouvelle Version Majeure — v5.8.0

> **Version majeure**带来了全新的网站集成和多项改进！这个版本为服务器管理员和玩家带来了更丰富的功能体验。

---

## ✨ Nouvelles Fonctionnalités

### 🌐 Site Web FactionSite v2.0

L'intégration web a été entièrement refaite avec **FactionSite v2** !

- **Interface moderne** : Design rafraîchi avec une meilleure expérience utilisateur
- **Classements en temps réel** : Consultez les statistiques des joueurs et des factions directement depuis le site
- **Exploration interactive** : Carte des factions avec visualisation des territoires
- **Système d'arène** : Mode de jeu compétitif avec arènes personnalisables
- **API de synchronisation** : Synchronisation bidirectionnelle entre le serveur Minecraft et le site web
- **Pages de faction** : Profils détaillés pour chaque faction avec stats, membres et historique

### 🎮 Mode Arène

- **Arènes de combat** dédiées avec spawns configurables
- **Système de matchmaking** pour trouver des adversaires
- **Statistiques de combat** détaillées par session d'arène

### 📊 Améliorations des Statistiques

- **Dashboard joueur enrichi** : Plus de métriques disponibles
- **Historique des performances** : Suivi de l'évolution des stats dans le temps
- **Export des données** : Possibilité d'exporter les statistiques

### 🛡️ Optimisations de Sécurité

- **Validation renforcée** des entrées utilisateur
- **Protection contre les exploits** courants
- **Amélioration de la sécurité** dans les échanges et transactions

---

## 🔧 Améliorations Techniques

### Performance
- **Optimisation des requêtes** base de données
- **Mise en cache améliorée** pour les classements
- **Réduction de la latence** dans les interfaces GUI

### Stabilité
- **Gestion des erreurs** plus robuste
- **Logging enrichi** pour le débogage
- **Protection contre les crashs** dans les opérations critiques

### Code
- **Refactorisation** du module de synchronisation web
- **Amélioration de la lisibilité** du code
- **Documentation** mise à jour

---

## 🐛 Corrections

- Correction de bugs dans le système de téléportation TPA
- Résolution de problèmes de visualisation des claims
- Correction des lags dans les GUI paginés
- Amélioration de la stabilité du système de guerre

---

## 📦 Contenu de la Release

| Fichier | Description |
|---------|-------------|
| `FactionPlugin-5.8.0.jar` | Plugin principal pour Paper/Spigot 1.21 |
| `FactionSite-v2.zip` | Site web complet pour la synchronisation |

---

## 🔗 Installation

### Plugin Serveur

1. Téléchargez `FactionPlugin-5.8.0.jar` depuis la [page des releases](../../releases)
2. Placez le fichier dans le dossier `plugins` de votre serveur
3. Redémarrez votre serveur
4. Configurez la liaison avec le site web via `/lier`

### Site Web

1. Extrayez le contenu de `FactionSite-v2.zip`
2. Configurez `src/.env` avec les paramètres de votre serveur
3. Lancez `npm install` puis `npm start`
4. Accédez à votre site via le port configuré (par défaut: 3000)

---

## 🚀 Mise à Jour depuis une Version Antérieure

Si vous mettez à jour depuis la version 5.7.2 ou antérieure :

1. **Sauvegardez** vos fichiers de configuration dans `plugins/FactionPlugin/`
2. **Supprimez** l'ancien fichier `.jar`
3. **Installez** la nouvelle version
4. **Redémarrez** le serveur
5. Les fichiers de config seront automatiquement migrés

---

## ⚠️ Notes Importantes

- **Java 21 requis** : Cette version nécessite Java 21 pour fonctionner
- **Paper 1.21+** : Compatible uniquement avec Paper ou Spigot 1.21 et supérieur
- **Base de données** : Aucune migration de base de données requise

---

## 📨 Rapports de Bugs & Suggestions

Si vous trouvez un bug ou avez une suggestion, n'hésitez pas à :
- Ouvrir une issue sur [GitHub](https://github.com/axblrd/Facc/issues)
- Soumettre une pull request

---

**Amusez-vous bien sur votre serveur Factions ! ⚔️🏰