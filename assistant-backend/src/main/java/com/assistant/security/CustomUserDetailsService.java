package com.assistant.security;

import com.assistant.entity.User;
import com.assistant.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Collections;

/**
 * Service personnalisé pour charger les détails d'un utilisateur à partir de l'email
 * Implémente l'interface UserDetailsService de Spring Security
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Charge un utilisateur par son email (utilisé comme username)
     * @param email email de l'utilisateur
     * @return UserDetails compréhensible par Spring Security
     * @throws UsernameNotFoundException si l'utilisateur n'existe pas
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Recherche de l'utilisateur en base de données
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé avec l'email : " + email));

        // Conversion de notre entité User vers un objet Spring Security UserDetails
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),                    // Nom d'utilisateur
                user.getMotDePasse(),               // Mot de passe (déjà hashé)
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole())) // Autorité = "ROLE_ETUDIANT" ou "ROLE_ENSEIGNANT"
        );
    }
}