import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

/**
 * Service d'authentification
 * Gère les appels API liés à l'authentification et la gestion du token
 * 
 * Fonctionnalités :
 * - Connexion (login)
 * - Inscription (register)
 * - Stockage local du token et des informations utilisateur
 * - Récupération du rôle
 * - Déconnexion
 */
@Injectable({ providedIn: 'root' })
export class AuthService {
  // URL de base du backend Spring Boot
  private API_URL = 'http://localhost:8080/api/auth';

  constructor(private http: HttpClient) {}

  /**
   * Authentification d'un utilisateur
   * @param credentials Email et mot de passe
   * @returns Observable contenant la réponse (token + infos utilisateur)
   */
  login(credentials: { email: string; motDePasse: string }): Observable<any> {
    return this.http.post(`${this.API_URL}/login`, credentials);
  }

  /**
   * Inscription d'un nouvel utilisateur
   * @param user Informations du nouvel utilisateur
   * @returns Observable contenant l'utilisateur créé
   */
  register(user: { nom: string; email: string; motDePasse: string; role: string }): Observable<any> {
    return this.http.post(`${this.API_URL}/register`, user);
  }

  /**
   * Sauvegarde les données utilisateur dans localStorage
   * Ces données persisteront même après fermeture du navigateur
   * @param data Réponse du serveur contenant token, role, id, nom
   */
  saveUser(data: any): void {
    localStorage.setItem('token', data.token);      // Token JWT pour authentification
    localStorage.setItem('role', data.role);        // Rôle (ETUDIANT ou ENSEIGNANT)
    localStorage.setItem('user_id', data.id);       // Identifiant utilisateur
    localStorage.setItem('userName', data.nom);     // Nom complet
  }

  /**
   * Récupère le rôle de l'utilisateur connecté
   * @returns Le rôle ou null si non connecté
   */
  getRole(): string | null {
    return localStorage.getItem('role');
  }

  /**
   * Déconnexion : suppression de toutes les données de session
   */
  logout(): void {
    localStorage.clear();
  }
}