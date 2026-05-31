package com.assistant.repository;

import com.assistant.dto.SessionParEtudiantDTO;
import com.assistant.entity.SessionCours;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

/**
 * Repository pour l'entité SessionCours
 */
public interface SessionCoursRepository extends JpaRepository<SessionCours, Long> {

    /**
     * Trouve toutes les sessions d'un utilisateur spécifique (par email)
     * Triées par date de début décroissante (les plus récentes en premier)
     * @param email email de l'utilisateur
     * @return liste des sessions
     */
    @Query("SELECT s FROM SessionCours s WHERE s.user.email = :email ORDER BY s.heureDebut DESC")
    List<SessionCours> findByUserEmailOrderByHeureDebutDesc(@Param("email") String email);

    /**
     * Récupère le nombre de sessions par étudiant (pour le dashboard enseignant)
     * @return liste des DTO SessionParEtudiant
     */
    @Query("""
        SELECT new com.assistant.dto.SessionParEtudiantDTO(
            s.user.nom,
            COUNT(s),
            s.user.role
        )
        FROM SessionCours s
        GROUP BY s.user.nom, s.user.role
    """)
    List<SessionParEtudiantDTO> getSessionsParEtudiant();
}