package com.assistant.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Configuration du client HTTP RestTemplate
 * Utilisé pour les appels HTTP synchrones vers le service FastAPI
 */
@Configuration
public class RestTemplateConfig {

    /**
     * Crée et configure un bean RestTemplate
     * Ce bean pourra être injecté dans les services qui ont besoin d'appeler des API externes
     * @return une instance configurée de RestTemplate
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}