package com.assistant.controller;

import com.assistant.dto.AiResponseDTO;
import com.assistant.dto.InteractionDTO;
import com.assistant.dto.QuestionRequest;
import com.assistant.entity.Interaction;
import com.assistant.service.AiService;
import com.assistant.service.InteractionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Contrôleur de l'assistant IA
 * Gère les interactions entre l'utilisateur et l'IA
 * Endpoint de base : /api/assistant
 */
@RestController
@RequestMapping("/api/assistant")
@CrossOrigin(origins = "http://localhost:4200")
public class InteractionController {

    private final InteractionService interactionService;
    private final AiService aiService;

    /**
     * Constructeur avec injection des dépendances
     * @param interactionService service de gestion des interactions
     * @param aiService service d'appel à l'API IA (FastAPI)
     */
    public InteractionController(
            InteractionService interactionService,
            AiService aiService
    ) {
        this.interactionService = interactionService;
        this.aiService = aiService;
    }

    /**
     * Récupère l'historique complet de toutes les interactions
     * Utilisé par le dashboard enseignant
     * @return Liste de tous les DTO d'interaction avec infos étudiant
     */
    @GetMapping("/historique")
    public ResponseEntity<List<InteractionDTO>> obtenirHistorique() {
        return ResponseEntity.ok(interactionService.recupererToutesLesInteractions());
    }

    /**
     * Récupère les interactions d'une session spécifique
     * @param sessionId identifiant de la session de cours
     * @return Liste des interactions de la session
     */
    @GetMapping("/session/{sessionId}/interactions")
    public ResponseEntity<List<Interaction>> obtenirInteractionsParSession(
            @PathVariable Long sessionId
    ) {
        return ResponseEntity.ok(interactionService.recupererInteractionsParSession(sessionId));
    }

    /**
     * Pose une question à l'assistant IA
     * 1. Appelle le service FastAPI pour obtenir la réponse et les métadonnées
     * 2. Sauvegarde l'interaction en base de données
     * 3. Retourne l'interaction complète avec les métadonnées
     *
     * @param request contient sessionId et question
     * @return ResponseEntity avec l'interaction sauvegardée ou message d'erreur
     */
    @PostMapping("/ask")
    public ResponseEntity<?> poserQuestion(@RequestBody QuestionRequest request) {
        try {
            // Étape 1 : Appel à l'API FastAPI pour obtenir la réponse
            AiResponseDTO aiResponse = aiService.askAi(request.getQuestion());

            // Étape 2 : Sauvegarde en base de données
            Interaction nouvelleInteraction = interactionService.enregistrerQuestion(
                    request.getSessionId(),
                    request.getQuestion(),
                    aiResponse.getReponse(),
                    aiResponse.getSection(),
                    aiResponse.getChap_name(),      // Nom du chapitre
                    aiResponse.getCategory(),        // Catégorie de la question
                    aiResponse.getSlide_id() != null
                            ? aiResponse.getSlide_id().longValue()  // Conversion Integer → Long
                            : null
            );

            // Étape 3 : Retourne l'interaction sauvegardée
            return ResponseEntity.ok(nouvelleInteraction);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Erreur : " + e.getMessage());
        }
    }
}