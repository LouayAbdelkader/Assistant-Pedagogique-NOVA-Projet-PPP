package com.assistant.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

/**
 * Filtre JWT interceptant chaque requête HTTP
 * Extrait et valide le token, puis charge l'utilisateur dans le contexte Spring Security
 */
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final CustomUserDetailsService userDetailsService;

    public JwtAuthFilter(JwtUtils jwtUtils, CustomUserDetailsService userDetailsService) {
        this.jwtUtils = jwtUtils;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Méthode principale du filtre exécutée pour chaque requête
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try {
            // 1. Extraire le token JWT du header Authorization
            String jwt = analyserJwt(request);

            // 2. Si token présent et valide, charger l'utilisateur
            if (jwt != null && jwtUtils.validerJwtToken(jwt)) {
                String email = jwtUtils.getEmailFromJwtToken(jwt);

                // 3. Charger les détails de l'utilisateur depuis la base
                UserDetails userDetails = userDetailsService.loadUserByUsername(email);

                // 4. Créer un token d'authentification Spring Security
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                // 5. Ajouter les détails de la requête
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // 6. Placer l'authentification dans le contexte Spring Security
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            System.err.println("Impossible de configurer l'authentification de l'utilisateur : " + e.getMessage());
        }

        // Continuer la chaîne des filtres
        filterChain.doFilter(request, response);
    }

    /**
     * Extrait le token JWT du header Authorization
     * Format attendu : "Bearer <token>"
     * @param request requête HTTP
     * @return token JWT ou null si absent
     */
    private String analyserJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (headerAuth != null && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7); // Supprime "Bearer " pour garder uniquement le token
        }
        return null;
    }
}