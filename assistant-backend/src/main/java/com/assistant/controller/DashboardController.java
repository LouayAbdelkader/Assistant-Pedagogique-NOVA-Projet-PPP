package com.assistant.controller;

import com.assistant.dto.QuestionParEtudiantDTO;
import com.assistant.dto.SessionParEtudiantDTO;
import com.assistant.service.AiAnalyticsService;
import com.assistant.service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.assistant.dto.TopQuestionDTO;
import java.security.Principal;
import java.util.List;
import java.util.Map;

/**
 * Contrôleur du tableau de bord
 * Fournit les statistiques et analytics pour les enseignants
 * Endpoint de base : /api/dashboard
 * Tous les endpoints sont sécurisés et nécessitent le rôle ENSEIGNANT
 */
@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "http://localhost:4200")
public class DashboardController {

    private final DashboardService dashboardService;
    private final AiAnalyticsService aiAnalyticsService;

    /**
     * Constructeur avec injection des dépendances
     * @param dashboardService service des statistiques du dashboard
     * @param aiAnalyticsService service d'analytics IA (questions fréquentes)
     */
    public DashboardController(
            DashboardService dashboardService,
            AiAnalyticsService aiAnalyticsService
    ) {
        this.dashboardService = dashboardService;
        this.aiAnalyticsService = aiAnalyticsService;
    }

    /**
     * Récupère les statistiques globales de la plateforme
     * @return Map contenant : nombreTotalSessions, nombreTotalQuestions, etudiantsActifs, dureeMoyenneSessions
     */
    @GetMapping("/statistiques")
    public ResponseEntity<Map<String, Object>> getStatistiquesGlobales() {
        return ResponseEntity.ok(dashboardService.getStatistiquesGlobales());
    }

    /**
     * Récupère les statistiques personnelles de l'utilisateur connecté
     * @param principal objet contenant l'email de l'utilisateur authentifié
     * @return Map contenant : nombreSessions, nombreQuestions, dureeTotale
     */
    @GetMapping("/mes-statistiques")
    public ResponseEntity<Map<String, Object>> getMesStatistiques(Principal principal) {
        String email = principal.getName(); // Récupère l'email depuis le token JWT
        return ResponseEntity.ok(dashboardService.getStatistiquesParUtilisateur(email));
    }

    /**
     * Récupère le nombre de questions posées par chaque étudiant
     * @return Liste des DTO avec nomEtudiant, nombreQuestions, role
     */
    @GetMapping("/questions-par-etudiant")
    public ResponseEntity<List<QuestionParEtudiantDTO>> getQuestionsParEtudiant() {
        return ResponseEntity.ok(dashboardService.getQuestionsParEtudiant());
    }

    /**
     * Récupère le nombre de questions posées aujourd'hui
     * @return Long représentant le nombre de questions du jour
     */
    @GetMapping("/questions-aujourdhui")
    public ResponseEntity<Long> getQuestionsAujourdHui() {
        return ResponseEntity.ok(dashboardService.getQuestionsAujourdHui());
    }

    /**
     * Récupère le nombre de sessions créées par chaque étudiant
     * @return Liste des DTO avec nomEtudiant, nombreSessions, role
     */
    @GetMapping("/sessions-par-etudiant")
    public ResponseEntity<List<SessionParEtudiantDTO>> getSessionsParEtudiant() {
        return ResponseEntity.ok(dashboardService.getSessionsParEtudiant());
    }

    /**
     * Récupère le top 5 des questions les plus fréquentes (via l'IA)
     * @return Liste des DTO avec question et count
     */
    @GetMapping("/top-questions")
    public ResponseEntity<List<TopQuestionDTO>> getTopQuestions() {
        return ResponseEntity.ok(aiAnalyticsService.getTopQuestions());
    }

    /**
     * Récupère le top 5 des questions posées par un étudiant spécifique
     * @param userId identifiant de l'étudiant
     * @return Liste des DTO avec question et count
     */
    @GetMapping("/top-questions/user/{userId}")
    public ResponseEntity<List<TopQuestionDTO>> getTopQuestionsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(aiAnalyticsService.getTopQuestionsByUser(userId));
    }
}