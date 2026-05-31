package com.assistant.service;

import com.assistant.dto.AiResponseDTO;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Map;

/**
 * Service de communication avec l'API IA (FastAPI)
 * Envoie les questions et reçoit les réponses avec métadonnées
 */
@Service
public class AiService {

    // RestTemplate pour les appels HTTP synchrones
    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * Envoie une question à l'API FastAPI
     * @param question texte de la question posée par l'utilisateur
     * @return DTO contenant la réponse et les métadonnées (chapitre, section, etc.)
     */
    public AiResponseDTO askAi(String question) {
        // URL du service FastAPI
        String url = "http://localhost:5000/predict";

        // Configuration des headers HTTP
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Corps de la requête JSON
        Map<String, String> body = Map.of("question", question);

        // Construction de l'entité HTTP
        HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

        // Appel POST au service FastAPI
        ResponseEntity<AiResponseDTO> response = restTemplate.postForEntity(
                url,
                request,
                AiResponseDTO.class
        );

        // Retourne le corps de la réponse
        return response.getBody();
    }
}