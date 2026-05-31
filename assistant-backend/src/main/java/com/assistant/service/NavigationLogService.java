package com.assistant.service;

import com.assistant.dto.LogRequest;
import com.assistant.entity.NavigationLog;
import com.assistant.entity.SessionCours;
import com.assistant.repository.NavigationLogRepository;
import com.assistant.repository.SessionCoursRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Service de gestion des logs de navigation
 * Enregistre les pages visitées par les utilisateurs
 */
@Service
public class NavigationLogService {

    private final NavigationLogRepository logRepository;
    private final SessionCoursRepository sessionCoursRepository;

    public NavigationLogService(NavigationLogRepository logRepository, SessionCoursRepository sessionCoursRepository) {
        this.logRepository = logRepository;
        this.sessionCoursRepository = sessionCoursRepository;
    }

    /**
     * Enregistre un log de navigation
     * @param request contient sessionId et pageVisitee
     */
    public void enregistrerLog(LogRequest request) {
        // Récupération de la session
        SessionCours session = sessionCoursRepository.findById(request.getSessionId())
                .orElseThrow(() -> new RuntimeException("Session introuvable"));

        // Création du log
        NavigationLog log = new NavigationLog();
        log.setSession(session);
        log.setPageVisitee(request.getPageVisitee());
        log.setTimestamp(LocalDateTime.now());

        // Sauvegarde
        logRepository.save(log);
    }

    /**
     * Récupère l'historique de navigation d'une session
     * @param sessionId identifiant de la session
     * @return liste des logs triés du plus récent au plus ancien
     */
    public List<NavigationLog> getHistoriqueSession(Long sessionId) {
        return logRepository.findBySessionIdOrderByTimestampDesc(sessionId);
    }
}