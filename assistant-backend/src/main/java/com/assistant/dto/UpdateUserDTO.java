package com.assistant.dto;

import lombok.Data;

/**
 * DTO pour la mise à jour du profil utilisateur
 * Accepte des champs optionnels (l'utilisateur peut modifier uniquement ce qu'il souhaite)
 */
@Data
public class UpdateUserDTO {
    private String nom;          // Nouveau nom (optionnel)
    private String motDePasse;   // Nouveau mot de passe (optionnel, minimum 5 caractères)
}