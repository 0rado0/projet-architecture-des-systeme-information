# Revue du projet Card-IO

> Ce projet a été réalisé en équipe de 4 personnes dans le cadre d’un travail collaboratif.

## Présentation générale

Card-IO est un projet de plateforme de gestion et d’échange de cartes, basé sur une architecture microservices. Il met en œuvre plusieurs services indépendants (authentification, gestion des utilisateurs, gestion des cartes, génération de cartes, boutique, reverse proxy) orchestrés via Docker et communiquant principalement en REST.

## Architecture

- **Reverse Proxy (nginx)** : point d’entrée unique, redirige les requêtes vers les bons services.
- **auth-service** : gère l’authentification et la sécurité.
- **user-service** : gère les utilisateurs et leurs profils.
- **card-service** : gère les cartes, leur attribution et leur gestion.
- **gencard-service** : génère de nouvelles cartes.
- **shop-service** : gère la boutique d’achat/vente de cartes.
- **static** : héberge les fichiers statiques (HTML, JS, CSS).

Chaque service est conteneurisé et peut être lancé indépendamment via Docker Compose.

## Points forts

- **Architecture modulaire** : chaque service est indépendant, facilitant la maintenance et l’évolution.
- **Scalabilité** : possibilité de répliquer les services selon la charge.
- **Utilisation de WebClient** : communication inter-services moderne et réactive.
- **Gestion des erreurs et résilience** : timeouts, circuit breaker (Resilience4j).
- **Reverse proxy** : centralisation des accès et sécurisation des flux.

## Limites et axes d’amélioration

- **Tests automatisés** : renforcer la couverture de tests unitaires et d’intégration.
- **Observabilité** : ajouter du monitoring (logs centralisés, métriques, alertes).
- **Sécurité** : approfondir la gestion des droits et la protection des API.
- **Documentation** : compléter la documentation fonctionnelle et technique pour chaque service.
- **CI/CD** : mettre en place une intégration et un déploiement continus.

## Lancement du projet

Prérequis : Docker et Docker Compose installés.

```bash
# Depuis la racine du projet
# Construction et lancement du reverse proxy (exemple)
docker build -t asi-reverse-proxy ./conf/reverse-proxy
# Lancement de tous les services
docker-compose up --build
```

Les services seront accessibles sur les ports définis dans `docker-compose.yml` (ex : Auth : 8081, User : 8082, etc.).

## Conclusion

Ce projet met en pratique les concepts modernes de microservices, la conteneurisation et la communication asynchrone. Il constitue une base solide pour une application évolutive et maintenable.
