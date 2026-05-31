package com.assistant.service;

import com.assistant.dto.TopQuestionDTO;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.List;

/**
 * Service d'analytics IA
 * Communique avec FastAPI pour récupérer les questions fréquentes
 */
@Service
public class AiAnalyticsService {

    private final RestTemplate restTemplate;

    public AiAnalyticsService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Récupère le top 5 des questions les plus posées globalement
     * @return liste des DTO TopQuestionDTO
     */
    public List<TopQuestionDTO> getTopQuestions() {
        String url = "http://localhost:5000/top-questions?k=5";

        ResponseEntity<List<TopQuestionDTO>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<TopQuestionDTO>>() {}
        );

        return response.getBody();
    }

    /**
     * Récupère le top 5 des questions les plus posées par un utilisateur spécifique
     * @param userId identifiant de l'utilisateur
     * @return liste des DTO TopQuestionDTO
     */
    public List<TopQuestionDTO> getTopQuestionsByUser(Long userId) {
        String url = "http://localhost:5000/top-questions/user/" + userId + "?k=5";

        ResponseEntity<List<TopQuestionDTO>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<TopQuestionDTO>>() {}
        );

        return response.getBody();
    }
}