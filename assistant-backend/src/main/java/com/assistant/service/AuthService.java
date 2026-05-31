package com.assistant.service;

import com.assistant.dto.JwtResponse;
import com.assistant.dto.LoginRequest;
import com.assistant.entity.User;
import com.assistant.repository.UserRepository;
import com.assistant.security.JwtUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

/**
 * Service d'authentification
 * Gère l'inscription et la connexion des utilisateurs
 */
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager,
                       JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    /**
     * Inscription d'un nouvel utilisateur
     * @param nouvelUtilisateur entité User à créer
     * @return utilisateur sauvegardé (sans modification de mot de passe)
     * @throws RuntimeException si email déjà utilisé ou mot de passe trop court
     */
    public User inscrireUtilisateur(User nouvelUtilisateur) {
        // Vérification de la longueur du mot de passe (minimum 5 caractères)
        if (nouvelUtilisateur.getMotDePasse() == null || nouvelUtilisateur.getMotDePasse().length() < 5) {
            throw new RuntimeException("Le mot de passe doit contenir au moins 5 caractères !");
        }

        // Vérification si l'email existe déjà
        Optional<User> utilisateurExistant = userRepository.findByEmail(nouvelUtilisateur.getEmail());
        if (utilisateurExistant.isPresent()) {
            throw new RuntimeException("Cet email est déjà utilisé !");
        }

        // Hashage du mot de passe avec BCrypt
        nouvelUtilisateur.setMotDePasse(passwordEncoder.encode(nouvelUtilisateur.getMotDePasse()));

        // Sauvegarde en base de données
        return userRepository.save(nouvelUtilisateur);
    }

    /**
     * Authentification d'un utilisateur
     * @param loginRequest contient email et motDePasse
     * @return JwtResponse contenant le token et les infos utilisateur
     * @throws RuntimeException si authentification échoue
     */
    public JwtResponse authentifierUtilisateur(LoginRequest loginRequest) {
        // Délègue l'authentification à Spring Security
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getMotDePasse()
                )
        );

        // Génération du token JWT
        String jwt = jwtUtils.genererJwtToken(authentication);

        // Récupération de l'utilisateur pour ses informations
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        return new JwtResponse(jwt, user.getId(), user.getNom(), user.getEmail(), user.getRole());
    }
}