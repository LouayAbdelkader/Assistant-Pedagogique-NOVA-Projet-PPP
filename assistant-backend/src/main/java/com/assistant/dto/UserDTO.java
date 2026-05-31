package com.assistant.dto;

import lombok.Data;

/**
 * DTO pour l'affichage des informations utilisateur
 * ⚠️ NE CONTIENT PAS le mot de passe pour des raisons de sécurité
 */
@Data
public class UserDTO {
    private Long id;       // Identifiant unique
    private String nom;    // Nom complet
    private String email;  // Email
    private String role;   // Rôle ("ETUDIANT" ou "ENSEIGNANT")
    // Remarque : Le mot de passe est volontairement absent pour protéger les données sensibles
}