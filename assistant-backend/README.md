
#  Assistant pédagogique pour le module Traitement du Signal - Backend

> API Spring Boot pour une plateforme d'assistant pédagogique intelligent avec intégration IA, authentification JWT, tableaux de bord analytiques et suivi des sessions d'apprentissage.

[![Java](https://img.shields.io/badge/Java-17-blue.svg)](https://openjdk.org/projects/jdk/17/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2-green.svg)](https://spring.io/projects/spring-boot)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-orange.svg)](https://www.mysql.com/)
[![FastAPI](https://img.shields.io/badge/FastAPI-0.100+-brightgreen.svg)](https://fastapi.tiangolo.com/)

---

## Table des matières

- [Présentation du projet](#présentation-du-projet)
- [Fonctionnalités principales](#fonctionnalités-principales)
- [Stack technique](#stack-technique)
- [Dépendances Maven](#dépendances-maven)
- [Architecture du projet](#architecture-du-projet)
- [Diagramme conceptuel des données](#diagramme-conceptuel-des-données)
- [Installation et démarrage](#installation-et-démarrage)
  - [Prérequis](#prérequis)
  - [Configuration de la base de données](#configuration-de-la-base-de-données)
  - [Configuration de l'application](#configuration-de-lapplication)
  - [Lancement du backend](#lancement-du-backend)
  - [Lancement du service IA (FastAPI)](#lancement-du-service-ia-fastapi)
- [API Endpoints](#api-endpoints)
  - [Authentification](#authentification)
  - [Assistant IA](#assistant-ia)
  - [Sessions de cours](#sessions-de-cours)
  - [Tableaux de bord](#tableaux-de-bord)
  - [Logs de navigation](#logs-de-navigation)
  - [Gestion des utilisateurs](#gestion-des-utilisateurs)
- [Sécurité](#sécurité)
- [Variables d'environnement](#variables-denvironnement)
- [Structure du projet](#structure-du-projet)
- [Bonus : Routes IA (FastAPI)](#bonus--routes-ia-fastapi)

---

## Présentation du projet

Ce projet est le **backend complet** d'une plateforme d'assistant pédagogique. Il permet :

- L'authentification sécurisée (élèves et enseignants) via **JWT**.
- L'intégration avec un **modèle IA** (FastAPI) qui répond aux questions des étudiants sur le contenu du cours et catégorise les questions par chapitre, section, et slide.
- Le suivi des **sessions d'apprentissage** : création de sessions, historique des questions, logs de navigation.
- Des **tableaux de bord analytiques** : statistiques globales et individuelles, questions les plus fréquentes, activité par étudiant.
- Une séparation claire des rôles : accès spécifiques pour les enseignants (analytique avancée, liste des étudiants) et les étudiants (interaction avec l'assistant, suivi personnel).

---

##  Fonctionnalités principales

###  Authentification et rôles
- Inscription et connexion sécurisées.
- Hashage des mots de passe (BCrypt).
- Génération et validation de tokens JWT.
- Rôles : `ETUDIANT` et `ENSEIGNANT`.

###  Assistant IA
- Envoi de questions à une API FastAPI.
- Récupération de réponses contextuelles avec métadonnées (chapitre, section, slide, catégorie).
- Sauvegarde automatique de chaque interaction en base de données.

###  Sessions d'apprentissage
- Création automatique d'une session à la première question.
- Génération automatique du nom de session à partir de la première question.
- Renommage et suppression de sessions.
- Historique complet des interactions par session.

###  Tableaux de bord
- **Enseignant** : statistiques globales (nombre total de sessions, questions, étudiants actifs, durée moyenne), questions par étudiant, sessions par étudiant, top questions posées, nombre de questions aujourd'hui.
- **Étudiant** : statistiques personnelles (nombre de sessions, nombre de questions, durée totale d'apprentissage).

###  Gestion des utilisateurs
- Modification du profil (nom, mot de passe).
- Liste des étudiants (réservée aux enseignants).

---

##  Stack technique

| Composant          | Technologie                                                                 |
| ------------------ | --------------------------------------------------------------------------- |
| **Langage**        | Java 17                                                                     |
| **Framework**      | Spring Boot 3.x                                                             |
| **Sécurité**       | Spring Security + JWT                                                       |
| **Base de données**| MySQL (via XAMPP) + Spring Data JPA / Hibernate                             |
| **Client HTTP**    | RestTemplate + WebClient (configuré)                                        |
| **Gestionnaire de dépendances** | Maven                                                           |
| **Service IA externe** | FastAPI (Python) - endpoints `/predict` et `/top-questions`            |
| **CORS**           | Configuré pour Angular (http://localhost:4200) et autres origines locales  |

---

##  Dépendances Maven
Voici la liste complète des dépendances utilisées dans le fichier `pom.xml` :
| Dépendance | Groupe ID | Version | Utilité |
|------------|-----------|---------|---------|
| **Spring Boot Starter Data JPA** | `org.springframework.boot` | 4.0.5 | Accès à la base de données avec Hibernate/JPA |
| **Spring Boot Starter Web MVC** | `org.springframework.boot` | 4.0.5 | Création des API REST (controllers) |
| **Spring Boot Starter Security** | `org.springframework.boot` | 4.0.5 | Authentification, autorisation, filtres de sécurité |
| **Spring Boot Starter WebFlux** | `org.springframework.boot` | 4.0.5 | Client WebClient réactif pour appels HTTP |
| **MySQL Connector J** | `com.mysql` | (géré par Spring) | Driver JDBC pour MySQL |
| **Lombok** | `org.projectlombok` | (géré par Spring) | Getters/Setters automatiques, constructeurs, `@Data` |
| **JJWT API** | `io.jsonwebtoken` | 0.11.5 | Création et validation des tokens JWT |
| **JJWT Impl** | `io.jsonwebtoken` | 0.11.5 | Implémentation de JJWT |
| **JJWT Jackson** | `io.jsonwebtoken` | 0.11.5 | Sérialisation JSON des claims JWT |
| **Spring Boot Starter Data JPA Test** | `org.springframework.boot` | 4.0.5 | Tests des couches repository |
| **Spring Boot Starter Web MVC Test** | `org.springframework.boot` | 4.0.5 | Tests des contrôleurs REST (MockMvc) |

##  Architecture du projet

L'architecture suit une approche **MVC (Modèle-Vue-Contrôleur)** avec des couches bien distinctes :

```
com.assistant
├── config/               # Configurations (RestTemplate, WebClient, Security)
├── controller/           # Contrôleurs REST (API endpoints)
│   ├── AuthController
│   ├── DashboardController
│   ├── InteractionController
│   ├── NavigationLogController
│   ├── SessionCoursController
│   └── UserController
├── dto/                  # Objets de transfert de données
├── entity/               # Entités JPA (Interactions, SessionCours, User, NavigationLog)
├── repository/           # Interfaces JPA Repository
├── security/             # Configuration Spring Security, JWT, CustomUserDetailsService
└── service/              # Logique métier (AuthService, AiService, DashboardService, ...)
```

---

##  Diagramme conceptuel des données

Les entités principales sont :

- **User** (`utilisateurs`) : `id`, `nom`, `email`, `motDePasse`, `role`
- **SessionCours** : `id`, `user_id` (FK), `nomSession`, `heureDebut`
- **Interaction** : `id`, `session_id` (FK), `question`, `reponse`, `section`, `chapitre`, `categorie`, `slideId`, `timestamp`
- **NavigationLog** : `id`, `session_id` (FK), `pageVisitee`, `timestamp`

Relations :
- Un `User` a plusieurs `SessionCours`.
- Une `SessionCours` a plusieurs `Interaction` et plusieurs `NavigationLog`.

---

##  Installation et démarrage

### Prérequis

- **JDK 17** ou supérieur
- **Maven** (ou utiliser le wrapper Maven)
- **XAMPP** (ou tout autre serveur MySQL) en cours d'exécution
- **Python 3.8+** (pour le service IA FastAPI)
- **Node.js** (pour le frontend Angular – facultatif pour ce README)

### Configuration de la base de données

1. Démarrer MySQL via XAMPP.
2. Créer la base de données :

```sql
CREATE DATABASE assistant_ts_db;
```

### Configuration de l'application

Le fichier `src/main/resources/application.properties` est déjà configuré avec :

```properties
spring.application.name=assistant-backend
spring.datasource.url=jdbc:mysql://localhost:3306/assistant_ts_db?serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

>  Si le mot de passe MySQL n'est pas vide, modifie la propriété `spring.datasource.password`.

### Lancement du backend

```bash
# Cloner le projet (ou se placer dans le répertoire source)
cd assistant-backend

# Compiler et lancer avec Maven
mvn spring-boot:run
```

Le backend sera accessible sur : `http://localhost:8080`

### Lancement du service IA (FastAPI)

```bash
assistant-pedagogique-main> python .\main.py
```
si n'est pas le meme dossier, juste se délacer vers le bon répertoire qui contient le fichier main.py en racine afin d'exécuter l'assistant et qui sera accessible via FastAPI
Le service IA sera accessible sur : `http://localhost:5000`

---

##  API Endpoints

### Authentification (`/api/auth`)

| Méthode | Endpoint         | Description                         | Rôle      |
| ------- | ---------------- | ----------------------------------- | --------- |
| POST    | `/register`      | Inscription d'un nouvel utilisateur | Public    |
| POST    | `/login`         | Connexion et obtention du token JWT | Public    |

### Assistant IA (`/api/assistant`)

| Méthode | Endpoint                         | Description                                      | Rôle          |
| ------- | -------------------------------- | ------------------------------------------------ | ------------- |
| GET     | `/historique`                    | Historique complet des interactions              | Authentifié   |
| GET     | `/session/{sessionId}/interactions` | Interactions d'une session spécifique        | Authentifié   |
| POST    | `/ask`                           | Poser une question à l'IA (corps : `sessionId`, `question`) | Authentifié |

### Sessions de cours (`/api/assistant/sessions`)

| Méthode | Endpoint                     | Description                            | Rôle          |
| ------- | ---------------------------- | -------------------------------------- | ------------- |
| GET     | `/`                          | Liste des sessions de l'utilisateur    | Authentifié   |
| POST    | `/`                          | Créer une nouvelle session             | Authentifié   |
| PUT     | `/{sessionId}/renommer`      | Renommer une session                   | Authentifié   |
| DELETE  | `/{sessionId}`               | Supprimer une session                  | Authentifié   |

### Tableaux de bord (`/api/dashboard`)

| Méthode | Endpoint                     | Description                               | Rôle          |
| ------- | ---------------------------- | ----------------------------------------- | ------------- |
| GET     | `/statistiques`              | Statistiques globales (tout utilisateur)  | ENSEIGNANT    |
| GET     | `/mes-statistiques`          | Statistiques personnelles                 | ENSEIGNANT    |
| GET     | `/questions-par-etudiant`    | Nombre de questions par étudiant          | ENSEIGNANT    |
| GET     | `/questions-aujourdhui`      | Nombre de questions posées aujourd'hui    | ENSEIGNANT    |
| GET     | `/sessions-par-etudiant`     | Nombre de sessions par étudiant           | ENSEIGNANT    |
| GET     | `/top-questions`             | Top 5 des questions les plus posées       | ENSEIGNANT    |
| GET     | `/top-questions/user/{userId}` | Top questions d'un étudiant spécifique  | ENSEIGNANT    |



### Gestion des utilisateurs (`/api/users`)

| Méthode | Endpoint      | Description                              | Rôle          |
| ------- | ------------- | ---------------------------------------- | ------------- |
| GET     | `/etudiants`  | Liste de tous les étudiants              | ENSEIGNANT    |
| PUT     | `/me`         | Modifier son profil (nom ou mot de passe)| Authentifié   |

---

##  Sécurité

- **Authentification** : JWT (tokens valides 24h).
- **Autorisation** : basée sur les rôles (`ROLE_ETUDIANT`, `ROLE_ENSEIGNANT`).
- **Filtre JWT** : intercepte chaque requête, valide le token et charge l'utilisateur.
- **CORS** : configuré pour autoriser `http://localhost:4200` (Angular) et d'autres origines locales.
- **Mots de passe** : hashés avec BCrypt (coût par défaut).
- **Protection CSRF** : désactivée (API stateless).

---

##  Variables d'environnement (recommandées)

Pour une meilleure sécurité, il est conseillé d'extraire les secrets dans des variables d'environnement :

```properties
JWT_SECRET=AssistantPedagogiqueSecretKeyPourLeProjetDeFinDetudes2024TresSecurisee
JWT_EXPIRATION_MS=86400000
DB_URL=jdbc:mysql://localhost:3306/assistant_ts_db?serverTimezone=UTC
DB_USERNAME=root
DB_PASSWORD=
```

---

##  Structure du projet (détail des packages)

```
src/
└── main/
    ├── java/com/assistant/
    │   ├── AssistantBackendApplication.java
    │   ├── config/
    │   ├── controller/
    │   ├── dto/
    │   ├── entity/
    │   ├── repository/
    │   ├── security/
    │   └── service/
    └── resources/
        └── application.properties
```

---

##  Bonus : Routes IA (FastAPI)

Le service IA séparé expose ces endpoints :

| Méthode | Endpoint                     | Description                                    |
| ------- | ---------------------------- | ---------------------------------------------- |
| POST    | `/predict`                   | Prend une `question` et retourne la réponse avec chapitre, section, slide, catégorie |
| GET     | `/top-questions?k=5`         | Retourne les k questions les plus fréquentes   |
| GET     | `/top-questions/user/{userId}?k=5` | Top questions posées par un utilisateur spécifique |

---



