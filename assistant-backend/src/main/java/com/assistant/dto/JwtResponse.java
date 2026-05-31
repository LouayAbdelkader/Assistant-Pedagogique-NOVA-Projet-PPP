package com.assistant.dto;

import lombok.Data;

/**
 * DTO de réponse après une authentification réussie
 * Contient le token JWT et les informations essentielles de l'utilisateur
 */
@Data
public class JwtResponse {
    private String token;    // Token JWT à utiliser pour les requêtes authentifiées
    private Long id;         // Identifiant de l'utilisateur
    private String nom;      // Nom complet
    private String email;    // Email
    private String role;     // Rôle ("ETUDIANT" ou "ENSEIGNANT")

    /**
     * Constructeur avec tous les champs
     * @param token JWT généré
     * @param id identifiant utilisateur
     * @param nom nom complet
     * @param email email
     * @param role rôle
     */
    public JwtResponse(String token, Long id, String nom, String email, String role) {
        this.token = token;
        this.id = id;
        this.nom = nom;
        this.email = email;
        this.role = role;
    }
}