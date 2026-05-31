package com.assistant.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Entité JPA représentant une session de cours
 * Une session regroupe plusieurs interactions (questions/réponses)
 */
@Entity
@Data
public class SessionCours {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;                     // Identifiant unique

    /**
     * Relation Many-to-One avec User
     * Un utilisateur (étudiant) peut avoir plusieurs sessions
     */
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;                   // Propriétaire de la session

    private String nomSession;           // Nom généré automatiquement (première question)
    private LocalDateTime heureDebut;    // Date et heure de création de la session

    /**
     * Relation One-to-Many avec Interaction
     * Une session peut avoir plusieurs interactions
     * CascadeType.ALL : les interactions sont supprimées si la session est supprimée
     * JsonIgnore : évite les boucles infinies lors de la sérialisation JSON
     */
    @JsonIgnore
    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL)
    private List<Interaction> interactions;

    /**
     * Relation One-to-Many avec NavigationLog
     * Une session peut avoir plusieurs logs de navigation
     */
    @JsonIgnore
    @OneToMany(mappedBy = "session", cascade = CascadeType.ALL)
    private List<NavigationLog> navigationLogs;

    /**
     * Calcule la date de la dernière activité de la session
     * @return la date de la dernière interaction, ou l'heure de début si aucune interaction
     */
    public LocalDateTime getDerniereActivite() {
        if (interactions != null && !interactions.isEmpty()) {
            return interactions.stream()
                    .map(Interaction::getTimestamp)
                    .max(LocalDateTime::compareTo)  // Prend la date la plus récente
                    .orElse(heureDebut);
        }
        return heureDebut;
    }

    /**
     * Calcule la durée de la session en minutes
     * @return durée entre heureDebut et dernière activité
     */
    public long getDureeEnMinutes() {
        LocalDateTime fin = getDerniereActivite();
        if (fin == null) return 0;
        return java.time.Duration.between(heureDebut, fin).toMinutes();
    }
}