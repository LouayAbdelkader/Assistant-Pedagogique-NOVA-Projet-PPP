package com.assistant.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Configuration du client HTTP WebClient (réactif)
 * Alternative plus moderne à RestTemplate pour les appels asynchrones
 */
@Configuration
public class WebClientConfig {

    /**
     * Crée un builder WebClient configurable
     * Permet une construction fluide des requêtes HTTP
     * @return un builder WebClient prêt à être utilisé
     */
    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }
}