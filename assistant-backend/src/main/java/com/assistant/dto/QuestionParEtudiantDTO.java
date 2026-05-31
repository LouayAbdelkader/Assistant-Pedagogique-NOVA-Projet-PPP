package com.assistant.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * DTO pour les statistiques : nombre de questions par étudiant
 * Utilisé dans le dashboard enseignant
 */
@Data
@AllArgsConstructor  // Génère un constructeur avec tous les paramètres
public class QuestionParEtudiantDTO {
    private String nomEtudiant;     // Nom de l'étudiant
    private Long nombreQuestions;   // Nombre total de questions posées
    private String role;            // Rôle (pour filtrer uniquement les étudiants)
}