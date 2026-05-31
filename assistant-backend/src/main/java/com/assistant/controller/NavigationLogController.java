package com.assistant.controller;

import com.assistant.dto.LogRequest;
import com.assistant.entity.NavigationLog;
import com.assistant.service.NavigationLogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Contrôleur des logs de navigation
 * Enregistre et consulte l'historique des pages visitées par les utilisateurs
 * Endpoint de base : /api/logs
 */
@RestController
@RequestMapping("/api/logs")
public class NavigationLogController {

    private final NavigationLogService logService;

    /**
     * Constructeur avec injection de dépendance
     * @param logService service de gestion des logs
     */
    public NavigationLogController(NavigationLogService logService) {
        this.logService = logService;
    }

    /**
     * Enregistre un log de navigation
     * POST http://localhost:8080/api/logs/enregistrer
     * @param request contient sessionId et pageVisitee
     * @return message de confirmation
     */
    @PostMapping("/enregistrer")
    public ResponseEntity<String> enregistrerLog(@RequestBody LogRequest request) {
        logService.enregistrerLog(request);
        return ResponseEntity.ok("Navigation enregistrée avec succès !");
    }

    /**
     * Récupère l'historique de navigation d'une session
     * GET http://localhost:8080/api/logs/session/{sessionId}
     * @param sessionId identifiant de la session
     * @return Liste des logs de navigation, triés du plus récent au plus ancien
     */
    @GetMapping("/session/{sessionId}")
    public ResponseEntity<List<NavigationLog>> getHistorique(@PathVariable Long sessionId) {
        return ResponseEntity.ok(logService.getHistoriqueSession(sessionId));
    }
}