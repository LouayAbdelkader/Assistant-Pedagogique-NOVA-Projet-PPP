package com.assistant.dto;

import lombok.Data;

/**
 * DTO pour l'enregistrement des logs de navigation
 */
@Data
public class LogRequest {
    private Long sessionId;      // Session de cours concernée
    private String pageVisitee;  // URL ou nom de la page visitée
}