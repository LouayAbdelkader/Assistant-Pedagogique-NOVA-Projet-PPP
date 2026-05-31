package com.assistant.controller;

import com.assistant.dto.UpdateUserDTO;
import com.assistant.dto.UserDTO;
import com.assistant.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;

/**
 * Contrôleur de gestion des utilisateurs
 * Permet la consultation et la modification des profils
 * Endpoint de base : /api/users
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    /**
     * Constructeur avec injection de dépendance
     * @param userService service de gestion des utilisateurs
     */
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Liste tous les étudiants (réservé aux enseignants)
     * GET http://localhost:8080/api/users/etudiants
     * @return Liste des DTO étudiants (sans le mot de passe)
     */
    @GetMapping("/etudiants")
    public ResponseEntity<List<UserDTO>> listerEtudiants() {
        List<UserDTO> etudiantsDto = userService.listerEtudiants();
        return ResponseEntity.ok(etudiantsDto);
    }

    /**
     * Modifie le profil de l'utilisateur connecté
     * PUT http://localhost:8080/api/users/me
     * @param dto contient les champs à modifier (nom, motDePasse)
     * @param principal objet contenant l'email de l'utilisateur
     * @return Le DTO utilisateur mis à jour (sans mot de passe)
     */
    @PutMapping("/me")
    public ResponseEntity<?> modifierProfil(
            @RequestBody UpdateUserDTO dto,
            Principal principal
    ) {
        String email = principal.getName();
        UserDTO updatedUser = userService.modifierProfil(email, dto);
        return ResponseEntity.ok(updatedUser);
    }
}