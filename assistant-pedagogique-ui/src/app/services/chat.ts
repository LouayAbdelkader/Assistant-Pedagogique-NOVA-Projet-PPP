import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

/**
 * Service de chat (Assistant IA)
 * Gère toutes les interactions avec l'assistant pédagogique
 * 
 * Fonctionnalités :
 * - Gestion des sessions (création, récupération)
 * - Envoi de questions à l'IA
 * - Récupération de l'historique
 * - Mise à jour du profil
 */
@Injectable({ providedIn: 'root' })
export class ChatService {
  // URL de base des endpoints de l'assistant
  private API_URL = 'http://localhost:8080/api/assistant';

  constructor(private http: HttpClient) {}

  /**
   * Récupère toutes les sessions de l'utilisateur connecté
   * @returns Liste des sessions (triées par date décroissante)
   */
  getSessions(): Observable<any[]> {
    return this.http.get<any[]>(`${this.API_URL}/sessions`);
  }

  /**
   * Crée une nouvelle session de discussion
   * @returns La session nouvellement créée
   */
  createSession(): Observable<any> {
    return this.http.post<any>(`${this.API_URL}/sessions`, {});
  }

  /**
   * Récupère l'historique des interactions d'une session
   * @param sessionId Identifiant de la session
   * @returns Liste des questions/réponses
   */
  getInteractions(sessionId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.API_URL}/session/${sessionId}/interactions`);
  }

  /**
   * Pose une question à l'assistant IA
   * @param sessionId Session active
   * @param question Texte de la question
   * @returns L'interaction complète (question + réponse + métadonnées)
   */
  askQuestion(sessionId: number, question: string): Observable<any> {
    return this.http.post<any>(`${this.API_URL}/ask`, { sessionId, question });
  }

  /**
   * Met à jour le profil utilisateur (nom et/ou mot de passe)
   * @param data Contient nom et/ou motDePasse
   * @returns Profil mis à jour
   */
  updateProfile(data: any): Observable<any> {
    return this.http.put('http://localhost:8080/api/users/me', data);
  }
}