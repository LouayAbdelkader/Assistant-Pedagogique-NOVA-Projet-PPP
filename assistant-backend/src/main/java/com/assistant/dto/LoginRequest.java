package com.assistant.dto;

import lombok.Data;

/**
 * DTO pour la requête de connexion
 * Contient les identifiants fournis par l'utilisateur
 */
@Data
public class LoginRequest {
    private String email;        // Email de l'utilisateur
    private String motDePasse;   // Mot de passe en clair (sera vérifié par Spring Security)
    private String action;       // Optionnel : pour logger l'action ("CLIC", "LECTURE", etc.)
}