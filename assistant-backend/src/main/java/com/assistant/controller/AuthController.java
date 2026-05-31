package com.assistant.controller;

import com.assistant.dto.LoginRequest;
import com.assistant.dto.JwtResponse;
import com.assistant.entity.User;
import com.assistant.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Contrôleur d'authentification
 * Gère l'inscription et la connexion des utilisateurs
 * Endpoint de base : /api/auth
 */
@CrossOrigin(origins = "http://localhost:4200") // Autorise les requêtes depuis Angular sur le port 4200
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    /**
     * Constructeur avec injection de dépendance
     * @param authService service d'authentification
     */
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Inscription d'un nouvel utilisateur
     * POST http://localhost:8080/api/auth/register
     * @param user objet User contenant nom, email, motDePasse, role
     * @return ResponseEntity avec l'utilisateur créé ou un message d'erreur
     */
    @PostMapping("/register")
    public ResponseEntity<?> inscrire(@RequestBody User user) {
        try {
            // Délègue l'inscription au service
            User nouvelUtilisateur = authService.inscrireUtilisateur(user);
            return ResponseEntity.ok(nouvelUtilisateur);
        } catch (RuntimeException e) {
            // Retourne une erreur 400 (Bad Request) si l'email existe déjà ou mot de passe invalide
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Connexion d'un utilisateur existant
     * POST http://localhost:8080/api/auth/login
     * @param loginRequest contient email et motDePasse
     * @return ResponseEntity avec le token JWT et les infos utilisateur
     */
    @PostMapping("/login")
    public ResponseEntity<?> authentifier(@RequestBody LoginRequest loginRequest) {
        try {
            // Délègue l'authentification au service
            JwtResponse response = authService.authentifierUtilisateur(loginRequest);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // Retourne une erreur 401 (Unauthorized) si les identifiants sont incorrects
            return ResponseEntity.status(401).body("Erreur d'authentification : Email ou mot de passe incorrect.");
        }
    }
}