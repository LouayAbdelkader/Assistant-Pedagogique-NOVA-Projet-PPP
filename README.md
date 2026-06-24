
# Assistant Pédagogique - Projet Complet

> Plateforme d'assistant pédagogique intelligent pour le module **Traitement du Signal** – Solution fullstack avec backend Spring Boot, frontend Angular et **module IA en Python/FastAPI**.

[![Java](https://img.shields.io/badge/Java-17-blue.svg)](https://openjdk.org/projects/jdk/17/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.5-green.svg)](https://spring.io/projects/spring-boot)
[![Angular](https://img.shields.io/badge/Angular-17-red.svg)](https://angular.io/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-orange.svg)](https://www.mysql.com/)
[![Tailwind CSS](https://img.shields.io/badge/Tailwind_CSS-3.4-06B6D4.svg)](https://tailwindcss.com/)
[![Python](https://img.shields.io/badge/Python-3.11-blue.svg)](https://www.python.org/)
[![FastAPI](https://img.shields.io/badge/FastAPI-0.115-green.svg)](https://fastapi.tiangolo.com/)
[![Ollama](https://img.shields.io/badge/Ollama-0.6-orange.svg)](https://ollama.com/)

---

## Table des matières

- [Présentation du projet](#présentation-du-projet)
- [Structure du projet](#structure-du-projet)
- [Documentation détaillée](#documentation-détaillée)
- [Prérequis](#prérequis)
- [Installation et démarrage](#installation-et-démarrage)
  - [1. Base de données MySQL](#1-base-de-données-mysql)
  - [2. Backend Spring Boot](#2-backend-spring-boot)
  - [3. Service IA FastAPI](#3-service-ia-fastapi)
  - [4. Frontend Angular](#4-frontend-angular)
- [Architecture globale](#architecture-globale)
- [Technologies utilisées](#technologies-utilisées)

---

## Présentation du projet

Ce projet est une plateforme complète d'assistant pédagogique intelligent dédiée au module **Traitement du Signal**. Il permet aux étudiants de poser des questions sur le contenu du cours et de recevoir des réponses contextuelles, tout en offrant aux enseignants un tableau de bord analytique pour suivre l'activité des étudiants.

### Fonctionnalités clés

| Côté | Fonctionnalités |
|------|-----------------|
| **Backend** | API REST, authentification JWT, gestion des sessions, logs de navigation, endpoints analytics |
| **Frontend** | Interface de chat moderne, dashboard enseignant avec graphiques, gestion de profil |
| **IA** | Service FastAPI pour la génération de réponses et l'analyse des questions fréquentes |

---

## Structure du projet

```
projet/
├── assistant-backend/          # Backend Spring Boot
│   └── README.md               # Documentation détaillée du backend
├── assistant-pedagogique-ui/   # Frontend Angular
│   └── README.md               # Documentation détaillée du frontend
├── assistant-pedagogique-ia/   # Module IA
│   └── README.md               # Documentation détaillée du module IA
└── README.md                   # Ce fichier (vue d'ensemble)
```

| Dossier | Description | README |
|---------|-------------|--------|
| `assistant-backend/` | API Spring Boot (JWT, JPA, sécurité) | [Voir README](./assistant-backend/README.md) |
| `assistant-pedagogique-ui/` | Interface Angular (Tailwind, Chart.js) | [Voir README](./assistant-pedagogique-ui/README.md) |
| `assistant-pedagogique-ai/` | Service IA (FastAPI, embeddings, LLM) | [Voir README](./assistant-pedagogique-ai/README.md) |

---

## Documentation détaillée

Chaque sous-projet contient son propre README avec des instructions spécifiques :

### 📁 assistant-backend/README.md
- Installation et configuration de Spring Boot
- Configuration de la base de données MySQL
- Liste des dépendances Maven
- Tous les endpoints API documentés
- Configuration de la sécurité JWT
- Structure complète du backend

### 📁 assistant-pedagogique-ui/README.md
- Installation de Node.js et Angular CLI
- Configuration de Tailwind CSS
- Lancement du serveur de développement
- Structure des composants Angular
- Documentation des services et intercepteurs
- Commandes PowerShell (Set-ExecutionPolicy)

### 📁 assistant-ia/README.md
- Installation de Python et des dépendances
- Configuration de MySQL (fichier `config.py`)
- Téléchargement du modèle spaCy (`fr_core_news_sm`)
- Installation d’Ollama et du modèle `llama3.2`
- Exécution des notebooks de prétraitement et vectorisation
- Lancement du serveur FastAPI (`python main.py`)
- Documentation des endpoints `/predict` et `/top-questions`

---

## Prérequis

Avant de commencer, assurez-vous d'avoir installé :

| Logiciel | Version | Utilité |
|----------|---------|---------|
| **Java JDK** | 17+ | Backend Spring Boot |
| **Maven** | 3.8+ | Build du backend |
| **MySQL** | 8.0+ | Base de données (via XAMPP) |
| **Node.js** | 18+ | Frontend Angular |
| **Angular CLI** | 17+ | Serveur de développement |
| **Python** | 3.9+ | Service IA FastAPI |

---

## Installation et démarrage

### 1. Base de données MySQL

```sql
-- Démarrer MySQL (via XAMPP)
-- Créer la base de données
CREATE DATABASE assistant_ts_db;
```

### 2. Backend Spring Boot

```bash
cd assistant-backend

# Configuration (fichier application.properties déjà prêt)
# spring.datasource.url=jdbc:mysql://localhost:3306/assistant_ts_db
# spring.datasource.username=root
# spring.datasource.password=

# Lancer l'application
mvn spring-boot:run
```

Le backend tourne sur : `http://localhost:8080`

> 📖 Plus de détails dans [assistant-backend/README.md](./assistant-backend/README.md)

### 3. Service IA FastAPI

```bash
cd assistant-pedagogique-ia

# (Optionnel) Créer un environnement virtuel
python -m venv venv
source venv/bin/activate  # ou venv\Scripts\activate sous Windows

# Installer les dépendances (la commande détaillée dans le README)

# Configurer les identifiants MySQL dans src/config.py

# Lancer le service
python main.py
```
Le service IA tourne sur : `http://localhost:5000`

### 4. Frontend Angular

```bash
cd assistant-pedagogique-ui

# Windows PowerShell (une seule fois)
Set-ExecutionPolicy -Scope CurrentUser -ExecutionPolicy RemoteSigned

# Installation des dépendances
npm install

# Lancement du serveur
ng serve
```

Le frontend est accessible sur : `http://localhost:4200`

> 📖 Plus de détails dans [assistant-pedagogique-ui/README.md](./assistant-pedagogique-ui/README.md)

---

## Architecture globale

```
┌─────────────────────────────────────────────────────────────────┐
│                   UTILISATEUR (Etudiant-Enseignant)             │
└─────────────────────────────────────────────────────────────────┘
                                  │
                                  ▼
┌─────────────────────────────────────────────────────────────────┐
│                    FRONTEND (Angular 17)                         │
│                  http://localhost:4200                           │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────────────────┐  │
│  │    Login    │  │   Register  │  │         Chat            │  │
│  └─────────────┘  └─────────────┘  └─────────────────────────┘  │
│  ┌─────────────────────────────────────────────────────────────┐│
│  │              Dashboard Enseignant (Chart.js)                ││
│  └─────────────────────────────────────────────────────────────┘│
└─────────────────────────────────────────────────────────────────┘
                                  │
                                  │ HTTP / JWT
                                  ▼
┌─────────────────────────────────────────────────────────────────┐
│                    BACKEND (Spring Boot 4.0.5)                   │
│                  http://localhost:8080                           │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────────────────┐  │
│  │   Auth API  │  │   Chat API  │  │     Dashboard API       │  │
│  └─────────────┘  └─────────────┘  └─────────────────────────┘  │
│  ┌─────────────────────────────────────────────────────────────┐│
│  │              Security (JWT) + JPA + MySQL                   ││
│  └─────────────────────────────────────────────────────────────┘│
└─────────────────────────────────────────────────────────────────┘
                                  │
                                  │ HTTP
                                  ▼
┌─────────────────────────────────────────────────────────────────┐
│                    SERVICE IA (FastAPI)                          │
│                  http://localhost:5000                           │
│  ┌─────────────────────┐  ┌────────────────────────────────────┐│
│  │     /predict        │  │        /top-questions              ││
│  │  (Réponse aux QCM)  │  │    (Analyse questions fréquentes)  ││
│  └─────────────────────┘  └────────────────────────────────────┘│
└─────────────────────────────────────────────────────────────────┘
                                  │
                                  ▼
┌─────────────────────────────────────────────────────────────────┐
│                    BASE DE DONNÉES (MySQL)                       │
│                  assistant_ts_db                                 │
│  ┌──────────┐  ┌──────────────┐  ┌────────────────────────────┐  │
│  │utilisateurs│ │ session_cours│  │      interactions          │  │
│  └──────────┘  └──────────────┘  └────────────────────────────┘  │
└─────────────────────────────────────────────────────────────────┘
```

---

## Technologies utilisées

| Composant | Technologies |
|-----------|--------------|
| **Backend** | Spring Boot 4.0.5, Spring Security, JWT, JPA/Hibernate, MySQL |
| **Frontend** | Angular 17, TypeScript, Tailwind CSS, Chart.js |
| **Service IA** | FastAPI, Python, sentence-transformers, scikit-learn, pandas, Ollama (llama3.2) |
| **Base de données** | MySQL 8.0 (XAMPP) |

---

## Démarrage rapide (résumé)

```bash
# 1. Démarrer MySQL (XAMPP)

# 2. Lancer le backend
cd assistant-backend
mvn spring-boot:run

# 3. Lancer le service IA (dans un autre terminal)
cd assistant-pedagogique-ai
python main.py

# 4. Lancer le frontend (dans un autre terminal)
cd assistant-pedagogique-ui
npm install
ng serve
```

Puis ouvre le navigateur sur `http://localhost:4200`

