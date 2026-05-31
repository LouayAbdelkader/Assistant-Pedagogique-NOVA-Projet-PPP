package com.assistant.dto;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * DTO pour les interactions (questions/réponses)
 * Utilisé pour l'historique et les tableaux de bord
 * Lombok @Data génère automatiquement getters, setters, toString, equals, hashCode
 */
@Data
public class InteractionDTO {
    private Long id;                 // Identifiant unique
    private String question;         // Question posée par l'utilisateur
    private String reponse;          // Réponse de l'IA
    private LocalDateTime timestamp; // Date et heure de l'interaction

    // Informations sur l'étudiant (pour le dashboard enseignant)
    private String nomEtudiant;      // Nom de l'étudiant
    private String emailEtudiant;    // Email de l'étudiant
    private String roleEtudiant;     // Rôle (normalement "ETUDIANT")

    // Métadonnées du cours (fournies par l'IA)
    private Long slideId;            // Identifiant de la slide
    private String section;          // Section du cours
    private String chapitre;         // Nom du chapitre
    private String categorie;        // Catégorie de la question
}