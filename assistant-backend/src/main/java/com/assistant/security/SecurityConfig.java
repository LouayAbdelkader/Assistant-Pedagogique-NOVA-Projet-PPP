package com.assistant.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.Arrays;

/**
 * Configuration principale de la sécurité Spring
 * Définit les règles d'authentification, d'autorisation, CORS et JWT
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    /**
     * Fournit le gestionnaire d'authentification
     * @param authConfig configuration d'authentification
     * @return AuthenticationManager
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    /**
     * Fournit l'encodeur de mots de passe (BCrypt)
     * @return PasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configure la chaîne de filtres de sécurité
     * Définit les règles CORS, CSRF, sessions, permissions
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 1. Configuration CORS (permet à Angular de communiquer avec l'API)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // 2. Désactivation CSRF (API stateless sans session)
                .csrf(csrf -> csrf.disable())

                // 3. Politique de session : STATELESS (pas de session serveur)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 4. Règles d'autorisation des endpoints
                .authorizeHttpRequests(auth -> auth
                        // Profil utilisateur connecté - accessible à tous les authentifiés
                        .requestMatchers("/api/users/me").authenticated()

                        // Authentification - accès public
                        .requestMatchers("/api/auth/**").permitAll()

                        // Tableaux de bord et gestion utilisateurs - réservé aux enseignants
                        .requestMatchers("/api/users/**", "/api/dashboard/**", "/api/logs/utilisateur/**")
                        .hasRole("ENSEIGNANT")

                        // Assistant et logs - accessible à tous les utilisateurs authentifiés
                        .requestMatchers("/api/assistant/**", "/api/logs/enregistrer")
                        .authenticated()

                        // Toutes les autres requêtes nécessitent une authentification
                        .anyRequest().authenticated()
                );

        // Ajout du filtre JWT avant le filtre d'authentification standard
        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Configuration CORS pour autoriser Angular (frontend) à communiquer avec l'API
     * @return CorsConfigurationSource
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Origines autorisées (Angular sur port 4200, etc.)
        configuration.setAllowedOrigins(Arrays.asList(
                "http://localhost:3000",
                "http://localhost:4200",      // Port par défaut d'Angular
                "http://localhost:5173",
                "http://localhost:5500",
                "http://127.0.0.1:5500"
        ));

        // Méthodes HTTP autorisées
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // Headers autorisés
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "Accept", "X-Requested-With"));

        // Headers exposés au client
        configuration.setExposedHeaders(Arrays.asList("Authorization", "Content-Type"));

        // Autorisation des credentials (cookies, headers d'auth)
        configuration.setAllowCredentials(true);

        // Durée de cache de la réponse preflight (3600 secondes = 1 heure)
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}