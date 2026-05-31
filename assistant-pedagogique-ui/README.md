# Assistant Pédagogique pour le module Traitement du Signal - Frontend Angular

> Interface utilisateur moderne et réactive pour une plateforme d'assistant pédagogique intelligent avec intégration IA, dashboards analytiques et gestion de sessions d'apprentissage.

[![Angular](https://img.shields.io/badge/Angular-17-red.svg)](https://angular.io/)
[![TypeScript](https://img.shields.io/badge/TypeScript-5.0-blue.svg)](https://www.typescriptlang.org/)
[![Tailwind CSS](https://img.shields.io/badge/Tailwind_CSS-3.4-06B6D4.svg)](https://tailwindcss.com/)
[![Chart.js](https://img.shields.io/badge/Chart.js-4.4-FF6384.svg)](https://www.chartjs.org/)

---

## Table des matières

- [Présentation du projet](#présentation-du-projet)
- [Fonctionnalités principales](#fonctionnalités-principales)
- [Stack technique](#stack-technique)
- [Architecture du projet](#architecture-du-projet)
- [Installation et démarrage](#installation-et-démarrage)
  - [Prérequis](#prérequis)
  - [Installation de Node.js et Angular CLI](#installation-de-nodejs-et-angular-cli)
  - [Configuration du projet](#configuration-du-projet)
  - [Lancement de l'application](#lancement-de-lapplication)
  - [Dépannage](#dépannage)
- [Structure détaillée des composants](#structure-détaillée-des-composants)
  - [Component Login](#component-login)
  - [Component Register](#component-register)
  - [Component Chat](#component-chat)
  - [Component Dashboard Enseignant](#component-dashboard-enseignant)
- [Services et Intercepteurs](#services-et-intercepteurs)
- [Routage et Guards](#routage-et-guards)
- [Styles et Thème](#styles-et-thème)
- [Communication avec l'API Backend](#communication-avec-lapi-backend)


---

## Présentation du projet

Ce projet est l'**interface utilisateur Angular** complète d'une plateforme d'assistant pédagogique intelligent pour le module **Traitement du Signal**. Elle permet :

- Une authentification fluide avec gestion de tokens JWT
- Une interface de chat moderne pour interagir avec l'assistant IA
- Un tableau de bord analytique pour les enseignants avec graphiques interactifs
- La gestion complète des sessions d'apprentissage (création, historique, renommage, suppression)
- La modification du profil utilisateur (nom et mot de passe)
- Une expérience utilisateur premium avec Tailwind CSS

---

## Fonctionnalités principales

### Authentification
- Page de connexion avec validation des champs
- Page d'inscription avec choix du rôle (Étudiant / Enseignant)
- Gestion sécurisée des tokens JWT
- Redirection automatique selon le rôle

### Assistant IA (Chat)
- Interface de chat moderne type messagerie
- Création et gestion de sessions de discussion
- Affichage des métadonnées enrichies (chapitre, section, catégorie, slide)
- Indicateur de frappe pendant la génération de la réponse
- Gestion de l'historique des conversations

### Dashboard Enseignant
- Statistiques globales (questions totales, étudiants actifs, questions du jour)
- Graphiques interactifs (Chart.js) :
  - Questions par étudiant (diagramme à barres)
  - Sessions par étudiant (diagramme à barres)
  - Top questions les plus posées (diagramme horizontal)
  - Top questions par étudiant sélectionné (diagramme circulaire)
- Liste des étudiants inscrits
- Historique des dernières questions posées
- Sélection d'un étudiant pour visualiser ses questions fréquentes

### Gestion de profil
- Modification du nom d'utilisateur
- Changement de mot de passe avec confirmation
- Champs de mot de passe avec affichage/masquage (toggle)

---

## Stack technique

| Composant            | Technologie                                    |
| -------------------- | ---------------------------------------------- |
| **Framework**        | Angular 17 (Standalone Components)            |
| **Langage**          | TypeScript 5.0                                |
| **Styling**          | Tailwind CSS 3.4 + CSS personnalisé           |
| **Graphiques**       | Chart.js 4.4                                  |
| **HTTP Client**      | Angular HttpClient + Interceptor JWT          |
| **Reactive Forms**   | Angular Reactive Forms                        |
| **Routing**          | Angular Router + Auth Guard                   |
| **Build Tool**       | Angular CLI                                   |

---

## Architecture du projet

```
app/
├── components/
│   ├── login/
│   │   ├── login.html
│   │   ├── login.css
│   │   ├── login.ts
│   │   └── login.spec.ts
│   ├── register/
│   │   ├── register.html
│   │   ├── register.css
│   │   ├── register.ts
│   │   └── register.spec.ts
│   ├── chat/
│   │   ├── chat.html
│   │   ├── chat.css
│   │   ├── chat.ts
│   │   └── chat.spec.ts
│   └── dashboard-enseignant/
│       ├── dashboard-enseignant.html
│       ├── dashboard-enseignant.css
│       ├── dashboard-enseignant.ts
│       └── dashboard-enseignant.spec.ts
├── interceptors/
│   ├── auth-interceptor.ts
│   └── auth-interceptor.spec.ts
├── services/
│   ├── auth.ts
│   ├── auth.spec.ts
│   ├── chat.ts
│   └── chat.spec.ts
├── app.config.ts
├── app.routes.ts
├── app.ts
├── auth-guard.ts
├── auth-guard.spec.ts
├── index.html
├── main.ts
└── styles.css
```

---

## Installation et démarrage

### Prérequis

Avant toute chose, vous devez avoir installé sur votre machine :

- **Node.js** (version 18.x ou supérieure) - [Télécharger depuis le site officiel](https://nodejs.org/)
- **npm** (inclus avec Node.js) ou **yarn**
- **Angular CLI** (installation globale recommandée)

### Installation de Node.js et Angular CLI

#### 1. Installation de Node.js

Rendez-vous sur le site officiel : [https://nodejs.org/](https://nodejs.org/)

- Téléchargez la version **LTS** (Long Term Support) recommandée
- Lancez l'installateur et suivez les instructions
- Vérifiez l'installation dans votre terminal :

```bash
node --version
# Affiche quelque chose comme v18.19.0

npm --version
# Affiche quelque chose comme 10.2.3
```

#### 2. Installation d'Angular CLI

Angular CLI est l'outil en ligne de commande officiel pour créer et gérer les projets Angular.

```bash
npm install -g @angular/cli
```

Vérifiez l'installation :

```bash
ng version
```

### Configuration du projet

#### 1. Créer un nouveau projet Angular (si ce n'est pas déjà fait)

```bash
ng new assistant-pedagogique-ui --standalone --routing --style=css
cd assistant-pedagogique-ui
```

#### 2. Installer les dépendances nécessaires

```bash
# Tailwind CSS
npm install -D tailwindcss postcss autoprefixer
npx tailwindcss init

# Chart.js pour les graphiques
npm install chart.js

# Autres dépendances (déjà présentes avec Angular)
# - @angular/forms pour les formulaires réactifs
# - @angular/common pour les directives
```

#### 3. Configurer Tailwind CSS

Dans le fichier `tailwind.config.js` :

```javascript
/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./src/**/*.{html,ts}",
  ],
  theme: {
    extend: {},
  },
  plugins: [],
}
```

Dans `styles.css`, ajoutez les directives Tailwind :

```css
@tailwind base;
@tailwind components;
@tailwind utilities;
```

#### 4. Copier la structure du projet

Remplacez le contenu du dossier `src/` par les fichiers fournis dans l'architecture ci-dessus.

### Lancement de l'application

> ⚠️ **Important pour les utilisateurs Windows PowerShell** :  
> Par défaut, PowerShell peut bloquer l'exécution des scripts. Vous devez d'abord autoriser l'exécution des scripts avec la commande suivante :

```powershell
Set-ExecutionPolicy -Scope CurrentUser -ExecutionPolicy RemoteSigned
```

Cette commande autorise l'exécution de scripts locaux et de scripts signés provenant d'Internet.  
**Explication des options :**
- `-Scope CurrentUser` : Applique la politique uniquement à l'utilisateur actuel
- `-ExecutionPolicy RemoteSigned` : Autorise les scripts locaux non signés, mais exige une signature pour les scripts téléchargés

Ensuite, démarrez le serveur de développement Angular :

```bash
# Démarrer le serveur de développement
ng serve
```
=> Port 4200 par défaut ! 

**Ordre complet des commandes pour Windows PowerShell :**

```powershell
# 1. Autoriser l'exécution des scripts (à faire une seule fois)
Set-ExecutionPolicy -Scope CurrentUser -ExecutionPolicy RemoteSigned

# 2. Naviguer vers le dossier du projet
cd chemin/vers/assistant-pedagogique-ui

# 3. Installer les dépendances (si ce n'est pas déjà fait)
npm install

# 4. Démarrer le serveur Angular
ng serve
```

**Pour les utilisateurs Mac / Linux :**

```bash
# Naviguer vers le dossier du projet
cd chemin/vers/assistant-pedagogique-ui

# Installer les dépendances
npm install

# Démarrer le serveur Angular
ng serve
```

L'application sera accessible à l'adresse : `http://localhost:4200`

> **Note importante** : Assurez-vous que le backend Spring Boot est en cours d'exécution sur `http://localhost:8080` avant de lancer l'application Angular.

### Dépannage

| Problème | Solution |
|----------|----------|
| `ng serve` ne fonctionne pas | Installez Angular CLI : `npm install -g @angular/cli` |
| Erreur de politique d'exécution PowerShell | Exécutez `Set-ExecutionPolicy -Scope CurrentUser -ExecutionPolicy RemoteSigned` |
| Port 4200 déjà utilisé | Utilisez un autre port : `ng serve --port 4201` |
| Erreur de connexion au backend | Vérifiez que Spring Boot tourne sur `http://localhost:8080` |
| Modules introuvables | Exécutez `npm install` pour installer les dépendances |
| Erreur CORS | Vérifiez que le backend a bien configuré CORS avec `http://localhost:4200` |
| Le chat ne répond pas | Vérifiez que le service FastAPI est bien démarré sur le port 5000 |

---

## Structure détaillée des composants

### Component Login

**Fichiers :** `login.html`, `login.css`, `login.ts`

**Fonctionnalités :**
- Formulaire réactif avec validation (email requis, mot de passe requis)
- Champ mot de passe avec toggle d'affichage (œil)
- Communication avec `AuthService` pour l'authentification
- Redirection dynamique selon le rôle (enseignant → `/dashboard`, étudiant → `/chat`)

**Points clés du code :**
```typescript
loginForm: FormGroup;
showPassword = false;

onSubmit() {
  const payload = {
    email: this.loginForm.value.email,
    motDePasse: this.loginForm.value.password
  };
  this.authService.login(payload).subscribe({
    next: (res) => {
      this.authService.saveUser(res);
      const role = this.authService.getRole();
      this.router.navigate(role === 'ENSEIGNANT' ? ['/dashboard'] : ['/chat']);
    }
  });
}
```

### Component Register

**Fichiers :** `register.html`, `register.css`, `register.ts`

**Fonctionnalités :**
- Formulaire complet (nom, email, mot de passe, rôle)
- Validation : email valide, mot de passe minimum 6 caractères
- Sélection visuelle du rôle (Étudiant / Enseignant avec style conditionnel)
- Gestion des erreurs avec messages utilisateur
- Redirection vers login après inscription réussie

### Component Chat

**Fichiers :** `chat.html`, `chat.css`, `chat.ts`

**Fonctionnalités :**
- Sidebar avec liste des sessions de discussion
- Création de nouvelle session
- Sélection et changement de session active
- Affichage de l'historique des messages (questions/réponses)
- Métadonnées enrichies (chapitre, section, catégorie, slide)
- Indicateur de frappe pendant la génération de réponse
- Modal de modification de profil
- Déconnexion

**Points clés :**
```typescript
sendMessage() {
  this.interactions.push({
    question: questionText,
    reponse: null,
    timestamp: new Date().toISOString()
  });
  this.isTyping = true;
  
  this.chatService.askQuestion(this.activeSession.id, questionText).subscribe({
    next: (res) => {
      this.interactions[index] = res;
      this.isTyping = false;
    }
  });
}
```

### Component Dashboard Enseignant

**Fichiers :** `dashboard-enseignant.html`, `dashboard-enseignant.css`, `dashboard-enseignant.ts`

**Fonctionnalités :**
- Chargement simultané de multiples endpoints HTTP (forkJoin)
- Affichage de 4 types de graphiques Chart.js :
  - Barres horizontales pour les questions par étudiant
  - Barres verticales pour les sessions par étudiant
  - Barres horizontales pour le top questions
  - Diagramme circulaire pour les questions d'un étudiant sélectionné
- Sélecteur d'étudiant pour visualiser ses questions fréquentes
- Liste des étudiants et historique des dernières questions
- Statistiques KPI (cartes récapitulatives)
- Rafraîchissement automatique lors de la navigation

**Points clés :**
```typescript
loadData(): void {
  forkJoin({
    historique: this.http.get(...),
    etudiants: this.http.get(...),
    questionsParEtudiant: this.http.get(...),
    // ...
  }).subscribe({
    next: (response) => {
      this.historique = response.historique;
      this.createQuestionsChart();
      this.createSessionsChart();
      // ...
    }
  });
}
```

---

## Services et Intercepteurs

### AuthService (`services/auth.ts`)

Méthodes principales :
- `login(credentials)` : appel POST à `/api/auth/login`
- `register(user)` : appel POST à `/api/auth/register`
- `saveUser(data)` : stockage du token et informations utilisateur dans localStorage
- `getRole()` : récupération du rôle de l'utilisateur connecté
- `logout()` : nettoyage du localStorage

### ChatService (`services/chat.ts`)

Méthodes principales :
- `getSessions()` : récupère toutes les sessions de l'utilisateur
- `createSession()` : crée une nouvelle session de discussion
- `getInteractions(sessionId)` : récupère l'historique d'une session
- `askQuestion(sessionId, question)` : envoie une question à l'IA
- `updateProfile(data)` : met à jour le profil utilisateur

### AuthInterceptor (`interceptors/auth-interceptor.ts`)

Fonctionnement :
- Intercepte toutes les requêtes HTTP sortantes
- Ajoute automatiquement le token JWT dans l'en-tête `Authorization: Bearer <token>`
- Exclut les endpoints d'authentification (`/auth/register`, `/auth/login`)

```typescript
intercept(req: HttpRequest<any>, next: HttpHandler) {
  const token = localStorage.getItem('token');
  if (token && !req.url.includes('/auth/')) {
    const cloned = req.clone({
      headers: req.headers.set('Authorization', `Bearer ${token}`)
    });
    return next.handle(cloned);
  }
  return next.handle(req);
}
```

---

## Routage et Guards

### Configuration des routes (`app.routes.ts`)

```typescript
export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: 'login', component: Login },
  { path: 'register', component: Register },
  { 
    path: 'dashboard', 
    component: DashboardEnseignant,
    canActivate: [authGuard]
  },
  { 
    path: 'chat', 
    component: ChatComponent,
    canActivate: [authGuard]
  },
  { path: '**', redirectTo: 'login' }
];
```

### AuthGuard (`auth-guard.ts`)

Protège les routes nécessitant une authentification :

```typescript
export const authGuard: CanActivateFn = (route, state) => {
  const token = localStorage.getItem('token');
  if (token) {
    return true;
  }
  return router.parseUrl('/login');
};
```

---

## Styles et Thème

### Tailwind CSS

Le projet utilise **Tailwind CSS** pour un styling utilitaire moderne :

- **Couleurs principales** : `#042C53` (bleu foncé), `#185FA5` (bleu moyen), `#F8FAFC` (gris clair)
- **Composants personnalisés** définis dans `styles.css` :
  - `.input-field` : champ de saisie premium
  - `.btn-primary` : bouton principal
  - `.glass-card` : effet glassmorphism

### Police

- Police principale : **Plus Jakarta Sans** (via Google Fonts)
- Police de secours : Inter, sans-serif

### Scrollbar personnalisée

```css
::-webkit-scrollbar {
  width: 6px;
}
::-webkit-scrollbar-thumb {
  background: #cbd5e1;
  border-radius: 10px;
}
```

---

## Communication avec l'API Backend

| Endpoint Backend | Méthode | Service Angular | Utilisation |
|-----------------|---------|-----------------|-------------|
| `/api/auth/login` | POST | `AuthService.login()` | Connexion |
| `/api/auth/register` | POST | `AuthService.register()` | Inscription |
| `/api/assistant/sessions` | GET | `ChatService.getSessions()` | Récupérer sessions |
| `/api/assistant/sessions` | POST | `ChatService.createSession()` | Créer session |
| `/api/assistant/session/{id}/interactions` | GET | `ChatService.getInteractions()` | Historique |
| `/api/assistant/ask` | POST | `ChatService.askQuestion()` | Poser question |
| `/api/users/etudiants` | GET | `DashboardEnseignant` | Liste étudiants |
| `/api/dashboard/questions-par-etudiant` | GET | `DashboardEnseignant` | Stats questions |
| `/api/dashboard/sessions-par-etudiant` | GET | `DashboardEnseignant` | Stats sessions |
| `/api/dashboard/top-questions` | GET | `DashboardEnseignant` | Top questions |
| `/api/dashboard/top-questions/user/{id}` | GET | `DashboardEnseignant` | Top questions par étudiant |
| `/api/users/me` | PUT | `ChatService.updateProfile()` | Modifier profil |

---

