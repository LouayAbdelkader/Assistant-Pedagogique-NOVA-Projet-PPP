package com.assistant.service;

import com.assistant.dto.UpdateUserDTO;
import com.assistant.dto.UserDTO;
import com.assistant.entity.User;
import com.assistant.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * Service de gestion des utilisateurs
 */
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Liste tous les étudiants (sans les mots de passe)
     * @return liste de DTO étudiants
     */
    public List<UserDTO> listerEtudiants() {
        List<User> etudiants = userRepository.findByRole("ETUDIANT");

        // Transformation en DTO pour cacher le mot de passe
        return etudiants.stream()
                .map(user -> {
                    UserDTO dto = new UserDTO();
                    dto.setId(user.getId());
                    dto.setNom(user.getNom());
                    dto.setEmail(user.getEmail());
                    dto.setRole(user.getRole());
                    return dto;
                })
                .toList();
    }

    /**
     * Modifie le profil d'un utilisateur
     * @param email email de l'utilisateur à modifier
     * @param dto données de mise à jour (nom, motDePasse)
     * @return DTO utilisateur mis à jour
     */
    @Transactional
    public UserDTO modifierProfil(String email, UpdateUserDTO dto) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        // Modification du nom si fourni
        if (dto.getNom() != null && !dto.getNom().trim().isEmpty()) {
            user.setNom(dto.getNom());
        }

        // Modification du mot de passe si fourni (minimum 5 caractères)
        if (dto.getMotDePasse() != null && dto.getMotDePasse().length() >= 5) {
            user.setMotDePasse(passwordEncoder.encode(dto.getMotDePasse()));
        }

        userRepository.save(user);

        // Retourne les informations sans le mot de passe
        UserDTO response = new UserDTO();
        response.setId(user.getId());
        response.setNom(user.getNom());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());

        return response;
    }
}