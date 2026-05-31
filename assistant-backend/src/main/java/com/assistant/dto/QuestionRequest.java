package com.assistant.dto;

import lombok.Data;

/**
 * DTO pour la requête de question à l'assistant IA
 */
@Data
public class QuestionRequest {
    private Long sessionId;   // Session dans laquelle la question est posée
    private String question;  // Texte de la question
}