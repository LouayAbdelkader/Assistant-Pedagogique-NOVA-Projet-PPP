package com.assistant.service;

import com.assistant.dto.QuestionParEtudiantDTO;
import com.assistant.dto.SessionParEtudiantDTO;
import com.assistant.entity.Interaction;
import com.assistant.entity.SessionCours;
import com.assistant.repository.InteractionRepository;
import com.assistant.repository.SessionCoursRepository;
import com.assistant.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service des tableaux de bord
 * Fournit les statistiques globales et par utilisateur
 */
@Service
public class DashboardService {

    private final InteractionRepository interactionRepository;
    private final SessionCoursRepository sessionCoursRepository;

    public DashboardService(InteractionRepository interactionRepository,
                            SessionCoursRepository sessionCoursRepository,
                            UserRepository userRepository) {
        this.interactionRepository = interactionRepository;
        this.sessionCoursRepository = sessionCoursRepository;
    }

    /**
     * Calcule les statistiques globales pour l'enseignant
     * @return Map contenant : nombreTotalSessions, nombreTotalQuestions, etudiantsActifs, dureeMoyenneSessions
     */
    public Map<String, Object> getStatistiquesGlobales() {
        Map<String, Object> stats = new HashMap<>();

        List<SessionCours> allSessions = sessionCoursRepository.findAll();
        List<Interaction> allInteractions = interactionRepository.findAll();

        // Nombre total d'étudiants actifs (qui ont au moins une session)
        long etudiantsActifs = allSessions.stream()
                .map(s -> s.getUser().getId())
                .distinct()
                .count();

        // Durée moyenne des sessions en minutes
        double dureeMoyenne = allSessions.stream()
                .mapToLong(SessionCours::getDureeEnMinutes)
                .average()
                .orElse(0);

        stats.put("nombreTotalSessions", allSessions.size());
        stats.put("nombreTotalQuestions", allInteractions.size());
        stats.put("etudiantsActifs", etudiantsActifs);
        stats.put("dureeMoyenneSessions", dureeMoyenne);
        return stats;
    }

    /**
     * Calcule les statistiques personnelles d'un utilisateur
     * @param email email de l'utilisateur
     * @return Map contenant : nombreSessions, nombreQuestions, dureeTotale
     */
    public Map<String, Object> getStatistiquesParUtilisateur(String email) {
        Map<String, Object> stats = new HashMap<>();

        List<SessionCours> userSessions = sessionCoursRepository.findByUserEmailOrderByHeureDebutDesc(email);
        List<Interaction> userInteractions = userSessions.stream()
                .flatMap(s -> s.getInteractions().stream())
                .collect(Collectors.toList());

        stats.put("nombreSessions", userSessions.size());
        stats.put("nombreQuestions", userInteractions.size());
        stats.put("dureeTotale", userSessions.stream().mapToLong(SessionCours::getDureeEnMinutes).sum());

        return stats;
    }

    /**
     * Récupère le nombre de questions posées par chaque étudiant
     */
    public List<QuestionParEtudiantDTO> getQuestionsParEtudiant() {
        return interactionRepository.getQuestionsParEtudiant();
    }

    /**
     * Récupère le nombre de questions posées aujourd'hui
     */
    public Long getQuestionsAujourdHui() {
        Object result = interactionRepository.countQuestionsAujourdHui();
        if (result == null) return 0L;
        return ((Number) result).longValue();
    }

    /**
     * Récupère le nombre de sessions par étudiant
     */
    public List<SessionParEtudiantDTO> getSessionsParEtudiant() {
        return sessionCoursRepository.getSessionsParEtudiant();
    }
}