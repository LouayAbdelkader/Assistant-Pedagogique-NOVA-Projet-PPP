package com.assistant.dto;

/**
 * DTO pour les questions les plus fréquentes (analytics IA)
 */
public class TopQuestionDTO {
    private String question;  // Texte de la question
    private Integer count;    // Nombre de fois que cette question a été posée

    // Constructeur par défaut (nécessaire pour la désérialisation JSON)
    public TopQuestionDTO() {
    }

    // Getters et Setters
    public String getQuestion() { return question; }
    public void setQuestion(String question) { this.question = question; }

    public Integer getCount() { return count; }
    public void setCount(Integer count) { this.count = count; }
}