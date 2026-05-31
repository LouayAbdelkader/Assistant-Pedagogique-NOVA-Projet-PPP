package com.assistant.service;

import com.assistant.entity.SessionCours;
import com.assistant.entity.User;
import com.assistant.repository.SessionCoursRepository;
import com.assistant.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Service de gestion des sessions de cours
 */
@Service
public class SessionCoursService {

    private final SessionCoursRepository sessionRepository;
    private final UserRepository userRepository;

    public SessionCoursService(SessionCoursRepository sessionRepository, UserRepository userRepository) {
        this.sessionRepository = sessionRepository;
        this.userRepository = userRepository;
    }

    /**
     * Récupère toutes les sessions d'un utilisateur
     * @param email email de l'utilisateur
     * @return liste des sessions triées par date décroissante
     */
    public List<SessionCours> recupererSessionsUtilisateur(String email) {
        return sessionRepository.findByUserEmailOrderByHeureDebutDesc(email);
    }

    /**
     * Crée une nouvelle session pour un utilisateur
     * @param email email de l'utilisateur
     * @return nouvelle session créée
     */
    @Transactional
    public SessionCours creerNouvelleSession(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        SessionCours nouvelleSession = new SessionCours();
        nouvelleSession.setUser(user);
        nouvelleSession.setHeureDebut(LocalDateTime.now());
        nouvelleSession.setNomSession(null); // Sera défini à la première question

        return sessionRepository.save(nouvelleSession);
    }

    /**
     * Renomme une session existante
     * @param sessionId identifiant de la session
     * @param nouveauNom nouveau nom
     * @param email email du propriétaire (vérification des droits)
     * @return session mise à jour
     */
    @Transactional
    public SessionCours renommerSession(Long sessionId, String nouveauNom, String email) {
        SessionCours session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session non trouvée"));

        // Vérification que l'utilisateur est bien le propriétaire
        if (!session.getUser().getEmail().equals(email)) {
            throw new RuntimeException("Vous n'êtes pas autorisé à modifier cette session");
        }

        session.setNomSession(nouveauNom);
        return sessionRepository.save(session);
    }

    /**
     * Génère automatiquement un nom de session à partir de la première question
     * @param sessionId identifiant de la session
     * @param premiereQuestion première question posée
     */
    @Transactional
    public void mettreAJourNomSession(Long sessionId, String premiereQuestion) {
        SessionCours session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session non trouvée"));

        if (session.getNomSession() == null || session.getNomSession().isEmpty()) {
            String nomGenere = genererNomSession(premiereQuestion);
            session.setNomSession(nomGenere);
            sessionRepository.save(session);
        }
    }

    /**
     * Génère un nom de session à partir de la question
     * @param question la première question
     * @return nom formaté (max 50 caractères)
     */
    private String genererNomSession(String question) {
        String cleaned = question.trim().replaceAll("\\s+", " ");

        if (cleaned.length() <= 50) {
            return cleaned;
        }
        return cleaned.substring(0, 47) + "...";
    }

    /**
     * Supprime une session et toutes ses interactions associées
     * @param sessionId identifiant de la session
     * @param email email du propriétaire (vérification des droits)
     */
    @Transactional
    public void supprimerSession(Long sessionId, String email) {
        SessionCours session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session non trouvée"));

        if (!session.getUser().getEmail().equals(email)) {
            throw new RuntimeException("Vous n'êtes pas autorisé à supprimer cette session");
        }

        sessionRepository.delete(session);
    }
}