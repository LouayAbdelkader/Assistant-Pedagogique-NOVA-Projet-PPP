package com.assistant.repository;

import com.assistant.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

/**
 * Repository pour l'entité User
 * Spring Data JPA génère automatiquement les implémentations
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Trouve un utilisateur par son email
     * Utilisé pour l'authentification et la récupération du profil
     * @param email email de l'utilisateur
     * @return Optional contenant l'utilisateur s'il existe
     */
    Optional<User> findByEmail(String email);

    /**
     * Liste tous les utilisateurs ayant un rôle spécifique
     * @param role le rôle à filtrer ("ETUDIANT" ou "ENSEIGNANT")
     * @return liste des utilisateurs correspondants
     */
    List<User> findByRole(String role);
}