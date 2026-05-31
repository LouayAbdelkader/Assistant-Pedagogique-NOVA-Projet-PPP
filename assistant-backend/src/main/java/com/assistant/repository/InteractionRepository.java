package com.assistant.repository;

import com.assistant.dto.QuestionParEtudiantDTO;
import com.assistant.entity.Interaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository pour l'entité Interaction
 * Hérite de JpaRepository qui fournit les méthodes CRUD de base
 */
public interface InteractionRepository extends JpaRepository<Interaction, Long> {

    /**
     * Trouve toutes les interactions d'une session spécifique
     * @param sessionId identifiant de la session
     * @return liste des interactions
     */
    List<Interaction> findBySessionId(Long sessionId);

    /**
     * Récupère le timestamp de la dernière interaction d'une session
     * @param sessionId identifiant de la session
     * @return date de la dernière interaction ou null
     */
    @Query("SELECT MAX(i.timestamp) FROM Interaction i WHERE i.session.id = :sessionId")
    LocalDateTime findMaxTimestampBySessionId(@Param("sessionId") Long sessionId);

    /**
     * Vérifie si une session a au moins une interaction
     * @param sessionId identifiant de la session
     * @return true si au moins une interaction existe
     */
    boolean existsBySessionId(Long sessionId);

    /**
     * Récupère le nombre de questions par étudiant (pour le dashboard enseignant)
     * Utilise une requête JPQL avec constructeur DTO
     * @return liste des DTO QuestionParEtudiant
     */
    @Query(""" 
        SELECT new com.assistant.dto.QuestionParEtudiantDTO(
            i.session.user.nom,
            COUNT(i),
            i.session.user.role
        )
        FROM Interaction i
        GROUP BY i.session.user.nom
        ORDER BY COUNT(i) DESC
    """)
    List<QuestionParEtudiantDTO> getQuestionsParEtudiant();

    /**
     * Compte le nombre de questions posées aujourd'hui par des étudiants
     * Utilise une requête SQL native pour CURDATE()
     * @return nombre de questions du jour
     */
    @Query(
            value = """
            SELECT COUNT(*)
            FROM interactions i
            JOIN session_cours s ON i.session_id = s.id
            JOIN utilisateurs u ON s.user_id = u.id
            WHERE u.role = 'ETUDIANT'
              AND i.timestamp >= CURDATE()
              AND i.timestamp < CURDATE() + INTERVAL 1 DAY
        """,
            nativeQuery = true
    )
    Long countQuestionsAujourdHui();
}