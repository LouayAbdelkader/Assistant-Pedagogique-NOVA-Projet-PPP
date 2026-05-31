package com.assistant.entity;

import jakarta.persistence.*;
import lombok.Data;

/**
 * Entité JPA représentant un utilisateur (Étudiant ou Enseignant)
 * Table associée : "utilisateurs"
 */
@Entity
@Data
@Table(name = "utilisateurs")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;                 // Identifiant unique auto-généré

    private String nom;              // Nom complet de l'utilisateur

    @Column(unique = true)           // Contrainte d'unicité en base de données
    private String email;            // Email (utilisé pour l'authentification)

    private String motDePasse;       // Mot de passe hashé avec BCrypt

    private String role;             // Rôle : "ETUDIANT" ou "ENSEIGNANT"
}