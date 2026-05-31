package com.assistant.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

/**
 * Gestionnaire global d'exceptions pour toute l'application
 *
 * @RestControllerAdvice permet d'intercepter les exceptions lancées par
 * n'importe quel contrôleur de l'application et de les traiter de manière centralisée.
 *
 * Avantages :
 * - Évite la duplication de code try/catch dans chaque contrôleur
 * - Standardise le format des réponses d'erreur
 * - Centralise la logique de logging et de monitoring
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Gère toutes les exceptions de type RuntimeException (et ses sous-classes)
     *
     * Cette méthode intercepte les erreurs métier personnalisées comme :
     * - "Session non trouvée"
     * - "Utilisateur non trouvé"
     * - "Email déjà utilisé"
     * - "Mot de passe incorrect"
     * - "Vous n'êtes pas autorisé"
     *
     * @param ex L'exception RuntimeException capturée
     * @param request La requête HTTP qui a déclenché l'exception
     * @return ResponseEntity contenant un ErrorResponse formaté avec le code HTTP 400 (Bad Request)
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex, WebRequest request) {

        // Construction de l'objet d'erreur standardisé
        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),                                    // Horodatage de l'erreur
                HttpStatus.BAD_REQUEST.value(),                         // Code HTTP 400
                "Requête invalide",                                      // Catégorie d'erreur
                ex.getMessage(),                                         // Message spécifique (ex: "Session 123 non trouvée")
                request.getDescription(false).replace("uri=", "")       // Extrait le chemin URL (supprime le préfixe "uri=")
        );

        // Retourne la réponse avec le statut HTTP 400 (Bad Request)
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    /**
     * Gère toutes les autres exceptions non prévues (Exception générique)
     *
     * Cette méthode agit comme un "filet de sécurité" qui attrape TOUTES les exceptions
     * qui n'ont pas été interceptées par les gestionnaires plus spécifiques.
     *
     * Exemples d'erreurs capturées ici :
     * - NullPointerException (bug dans le code)
     * - SQLException (problème de base de données)
     * - IOException (problème réseau/fichier)
     * - Toute erreur technique imprévue
     *
     * @param ex L'exception Exception capturée (générique)
     * @param request La requête HTTP qui a déclenché l'exception
     * @return ResponseEntity contenant un ErrorResponse formaté avec le code HTTP 500 (Internal Server Error)
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex, WebRequest request) {

        // Construction de l'objet d'erreur pour les erreurs serveur
        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),                                                    // Horodatage
                HttpStatus.INTERNAL_SERVER_ERROR.value(),                               // Code HTTP 500
                "Erreur Serveur",                                                       // Catégorie (erreur technique)
                "Une erreur interne est survenue : " + ex.getMessage(),                 // Message avec détails techniques
                request.getDescription(false).replace("uri=", "")                      // Chemin URL
        );

        // Retourne la réponse avec le statut HTTP 500 (Internal Server Error)
        // et de ne retourner qu'un message générique sans détails techniques
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}