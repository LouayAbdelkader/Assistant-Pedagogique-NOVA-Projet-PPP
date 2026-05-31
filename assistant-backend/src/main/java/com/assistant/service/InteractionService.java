package com.assistant.service;

import com.assistant.dto.InteractionDTO;
import com.assistant.entity.Interaction;
import com.assistant.entity.SessionCours;
import com.assistant.repository.InteractionRepository;
import com.assistant.repository.SessionCoursRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Service de gestion des interactions (questions/réponses)
 */
@Service
public class InteractionService {

    private final InteractionRepository interactionRepository;
    private final SessionCoursRepository sessionCoursRepository;
    private final SessionCoursService sessionCoursService;

    public InteractionService(
            InteractionRepository interactionRepository,
            SessionCoursRepository sessionCoursRepository,
            SessionCoursService sessionCoursService
    ) {
        this.interactionRepository = interactionRepository;
        this.sessionCoursRepository = sessionCoursRepository;
        this.sessionCoursService = sessionCoursService;
    }

    /**
     * Enregistre une nouvelle interaction (question/réponse) en base de données
     * Si c'est la première interaction de la session, génère automatiquement le nom de la session
     *
     * @param sessionId identifiant de la session
     * @param question question posée par l'utilisateur
     * @param reponse réponse générée par l'IA
     * @param section section du cours
     * @param chapitre chapitre du cours
     * @param categorie catégorie de la question
     * @param slideId identifiant de la slide (optionnel)
     * @return interaction sauvegardée
     */
    @Transactional
    public Interaction enregistrerQuestion(
            Long sessionId,
            String question,
            String reponse,
            String section,
            String chapitre,
            String categorie,
            Long slideId
    ) {
        // Récupération de la session
        SessionCours session = sessionCoursRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session non trouvée"));

        // Vérification si c'est la première interaction de la session
        boolean isFirstInteraction = !interactionRepository.existsBySessionId(sessionId);

        // Si première interaction et session sans nom, générer le nom
        if (isFirstInteraction && (session.getNomSession() == null || session.getNomSession().isEmpty())) {
            sessionCoursService.mettreAJourNomSession(sessionId, question);
            session = sessionCoursRepository.findById(sessionId).get();
        }

        // Création de l'interaction
        Interaction interaction = new Interaction();
        interaction.setQuestion(question);
        interaction.setReponse(reponse);
        interaction.setSection(section);
        interaction.setChapitre(chapitre);
        interaction.setCategorie(categorie);
        interaction.setSlideId(slideId);
        interaction.setTimestamp(LocalDateTime.now());
        interaction.setSession(session);

        return interactionRepository.save(interaction);
    }

    /**
     * Récupère toutes les interactions d'une session
     * @param sessionId identifiant de la session
     * @return liste des interactions
     */
    public List<Interaction> recupererInteractionsParSession(Long sessionId) {
        return interactionRepository.findBySessionId(sessionId);
    }

    /**
     * Récupère toutes les interactions avec enrichissement des infos étudiant
     * Utilisé pour l'historique du dashboard enseignant
     * @return liste de DTO InteractionDTO
     */
    public List<InteractionDTO> recupererToutesLesInteractions() {
        return interactionRepository.findAll()
                .stream()
                .map(inter -> {
                    InteractionDTO dto = new InteractionDTO();
                    dto.setId(inter.getId());
                    dto.setQuestion(inter.getQuestion());
                    dto.setReponse(inter.getReponse());
                    dto.setSection(inter.getSection());
                    dto.setChapitre(inter.getChapitre());
                    dto.setCategorie(inter.getCategorie());
                    dto.setSlideId(inter.getSlideId());
                    dto.setTimestamp(inter.getTimestamp());
                    dto.setRoleEtudiant(inter.getSession().getUser().getRole());

                    // Ajout des informations étudiant
                    if (inter.getSession() != null && inter.getSession().getUser() != null) {
                        dto.setNomEtudiant(inter.getSession().getUser().getNom());
                        dto.setEmailEtudiant(inter.getSession().getUser().getEmail());
                    } else {
                        dto.setNomEtudiant("Anonyme");
                    }

                    return dto;
                })
                .toList();
    }
}