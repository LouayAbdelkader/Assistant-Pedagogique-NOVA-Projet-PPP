package com.assistant.error;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * DTO (Data Transfer Object) pour les réponses d'erreur
 * Structure standardisée pour tous les messages d'erreur retournés par l'API
 *
 * Utilisation : Ce DTO est utilisé par le GlobalExceptionHandler pour formater
 * les erreurs de manière cohérente avant de les envoyer au client (Angular)
 */
@Data  // Lombok génère getters, setters, toString, equals, hashCode
@AllArgsConstructor  // Génère un constructeur avec tous les champs
public class ErrorResponse {

    /**
     * Date et heure exacte de l'occurrence de l'erreur
     * Format ISO-8601 (ex: 2024-01-15T14:30:45.123)
     * Utile pour le débogage et l'audit
     */
    private LocalDateTime timestamp;

    /**
     * Code HTTP de l'erreur (standard)
     * Exemples : 400 (Bad Request), 401 (Unauthorized), 404 (Not Found), 500 (Internal Error)
     */
    private int status;

    /**
     * Catégorie courte de l'erreur (phrase standard)
     * Exemples : "Requête invalide", "Non autorisé", "Ressource non trouvée"
     */
    private String error;

    /**
     * Message d'erreur détaillé (spécifique au contexte)
     * Exemple : "Session avec l'id 123 non trouvée"
     */
    private String message;

    /**
     * Chemin (URL) de la requête qui a provoqué l'erreur
     * Exemple : "/api/assistant/sessions/999/interactions"
     * Permet d'identifier rapidement l'endpoint problématique
     */
    private String path;
}