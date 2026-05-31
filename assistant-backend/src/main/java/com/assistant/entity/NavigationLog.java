package com.assistant.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * Entité JPA pour les logs de navigation
 * Enregistre les pages visitées par les utilisateurs pendant leurs sessions
 */
@Entity
@Data  // Lombok génère getters, setters, toString, equals, hashCode
public class NavigationLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;                    // Identifiant unique du log

    /**
     * Relation Many-to-One avec SessionCours
     * Un log appartient à une seule session
     * Une session peut avoir plusieurs logs
     */
    @ManyToOne
    @JoinColumn(name = "session_id")
    private SessionCours session;       // Session pendant laquelle la navigation a eu lieu

    private String pageVisitee;         // URL ou nom de la page visitée
    private LocalDateTime timestamp;    // Date et heure de la visite
}