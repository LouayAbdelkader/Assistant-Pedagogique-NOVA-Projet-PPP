package com.assistant.dto;

/**
 * DTO pour la réponse de l'API FastAPI
 * Contient la réponse IA ainsi que les métadonnées du cours
 */
public class AiResponseDTO {
    private Integer chap_id;      // Identifiant du chapitre
    private String chap_name;      // Nom du chapitre
    private Integer slide_id;      // Identifiant de la slide
    private String section;        // Section du cours
    private String category;       // Catégorie de la question
    private String reponse;        // Réponse générée par l'IA

    // Constructeur par défaut
    public AiResponseDTO() {
    }

    // Getters et Setters
    public Integer getChap_id() { return chap_id; }
    public void setChap_id(Integer chap_id) { this.chap_id = chap_id; }

    public String getChap_name() { return chap_name; }
    public void setChap_name(String chap_name) { this.chap_name = chap_name; }

    public Integer getSlide_id() { return slide_id; }
    public void setSlide_id(Integer slide_id) { this.slide_id = slide_id; }

    public String getSection() { return section; }
    public void setSection(String section) { this.section = section; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getReponse() { return reponse; }
    public void setReponse(String reponse) { this.reponse = reponse; }

    @Override
    public String toString() {
        return "AiResponseDTO{" +
                "chap_id=" + chap_id +
                ", chap_name='" + chap_name + '\'' +
                ", slide_id=" + slide_id +
                ", section='" + section + '\'' +
                ", category='" + category + '\'' +
                ", reponse='" + reponse + '\'' +
                '}';
    }
}