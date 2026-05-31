package com.assistant.controller;

import com.assistant.entity.SessionCours;
import com.assistant.service.SessionCoursService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Contrôleur de gestion des sessions de cours
 * Permet la création, modification, suppression et consultation des sessions
 * Endpoint de base : /api/assistant/sessions
 */
@RestController
@RequestMapping("/api/assistant/sessions")
@CrossOrigin(origins = "http://localhost:4200")
public class SessionCoursController {

    private final SessionCoursService sessionService;

    /**
     * Constructeur avec injection de dépendance
     * @param sessionService service de gestion des sessions
     */
    public SessionCoursController(SessionCoursService sessionService) {
        this.sessionService = sessionService;
    }

    /**
     * Récupère toutes les sessions de l'utilisateur connecté
     * @param principal objet contenant l'email de l'utilisateur
     * @return Liste des sessions de l'utilisateur
     */
    @GetMapping
    public ResponseEntity<List<SessionCours>> getSessions(Principal principal) {
        String email = principal.getName();
        return ResponseEntity.ok(sessionService.recupererSessionsUtilisateur(email));
    }

    /**
     * Crée une nouvelle session pour l'utilisateur connecté
     * @param principal objet contenant l'email de l'utilisateur
     * @return La session nouvellement créée
     */
    @PostMapping
    public ResponseEntity<SessionCours> createSession(Principal principal) {
        String email = principal.getName();
        SessionCours nouvelleSession = sessionService.creerNouvelleSession(email);
        return ResponseEntity.ok(nouvelleSession);
    }

    /**
     * Renomme une session existante
     * @param sessionId identifiant de la session à renommer
     * @param payload contient le nouveau nom dans la clé "nomSession"
     * @param principal objet contenant l'email pour vérifier les droits
     * @return La session mise à jour
     */
    @PutMapping("/{sessionId}/renommer")
    public ResponseEntity<SessionCours> renommerSession(
            @PathVariable Long sessionId,
            @RequestBody Map<String, String> payload,
            Principal principal
    ) {
        String nouveauNom = payload.get("nomSession");
        String email = principal.getName();
        SessionCours session = sessionService.renommerSession(sessionId, nouveauNom, email);
        return ResponseEntity.ok(session);
    }

    /**
     * Supprime une session (et toutes ses interactions associées)
     * @param sessionId identifiant de la session à supprimer
     * @param principal objet contenant l'email pour vérifier les droits
     * @return Message de confirmation
     */
    @DeleteMapping("/{sessionId}")
    public ResponseEntity<Map<String, String>> supprimerSession(
            @PathVariable Long sessionId,
            Principal principal
    ) {
        String email = principal.getName();
        sessionService.supprimerSession(sessionId, email);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Session supprimée avec succès");
        return ResponseEntity.ok(response);
    }

    /**
     * Calcule la durée d'une session (à implémenter)
     * @param sessionId identifiant de la session
     * @return Message indiquant que la fonctionnalité n'est pas encore implémentée
     */
    @GetMapping("/{sessionId}/duree")
    public ResponseEntity<Map<String, Object>> getDureeSession(@PathVariable Long sessionId) {
        // À implémenter : calculer la durée entre heureDebut et dernière interaction
        Map<String, Object> response = new HashMap<>();
        response.put("message", "À implémenter si nécessaire");
        return ResponseEntity.ok(response);
    }
}