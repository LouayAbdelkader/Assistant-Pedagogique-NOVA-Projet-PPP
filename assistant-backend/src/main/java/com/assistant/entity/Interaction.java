package com.assistant.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entité JPA représentant une interaction (question/réponse)
 * Table associée : "interactions"
 */
@Entity
@Table(name = "interactions")
public class Interaction {

    // =========================
    // IDENTIFIANT
    // =========================
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // =========================
    // CONTENU DE L'INTERACTION
    // =========================
    @Column(columnDefinition = "TEXT")
    private String question;          // Question posée par l'utilisateur

    @Column(columnDefinition = "LONGTEXT")
    private String reponse;           // Réponse générée par l'IA

    // =========================
    // MÉTADONNÉES DU COURS (fournies par l'IA)
    // =========================
    private String section;           // Section du cours concernée
    private String chapitre;          // Chapitre concerné
    private String categorie;         // Catégorie de la question
    private Long slideId;             // Slide spécifique (optionnel)

    // =========================
    // DATE DE L'INTERACTION
    // =========================
    private LocalDateTime timestamp;  // Date et heure de la question

    // =========================
    // RELATION AVEC LA SESSION
    // =========================
    @ManyToOne(fetch = FetchType.LAZY)  // Lazy loading pour optimiser les performances
    @JoinColumn(name = "session_id")
    private SessionCours session;      // Session de cours associée

    // =========================
    // CONSTRUCTEURS
    // =========================
    public Interaction() {
    }

    // =========================
    // GETTERS ET SETTERS
    // =========================
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getQuestion() { return question; }
    public void setQuestion(String question) { this.question = question; }

    public String getReponse() { return reponse; }
    public void setReponse(String reponse) { this.reponse = reponse; }

    public String getSection() { return section; }
    public void setSection(String section) { this.section = section; }

    public String getChapitre() { return chapitre; }
    public void setChapitre(String chapitre) { this.chapitre = chapitre; }

    public String getCategorie() { return categorie; }
    public void setCategorie(String categorie) { this.categorie = categorie; }

    public Long getSlideId() { return slideId; }
    public void setSlideId(Long slideId) { this.slideId = slideId; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public SessionCours getSession() { return session; }
    public void setSession(SessionCours session) { this.session = session; }
}