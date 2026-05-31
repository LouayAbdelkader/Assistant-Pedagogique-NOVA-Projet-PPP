package com.assistant.dto;

/**
 * DTO pour les statistiques : nombre de sessions par étudiant
 */
public class SessionParEtudiantDTO {
    private String nomEtudiant;     // Nom de l'étudiant
    private Long nombreSessions;    // Nombre de sessions créées
    private String role;            // Rôle (pour filtrer)

    /**
     * Constructeur avec tous les champs
     */
    public SessionParEtudiantDTO(String nomEtudiant, Long nombreSessions, String role) {
        this.nomEtudiant = nomEtudiant;
        this.nombreSessions = nombreSessions;
        this.role = role;
    }

    // Getters
    public String getNomEtudiant() { return nomEtudiant; }
    public Long getNombreSessions() { return nombreSessions; }
    public String getRole() { return role; }
}