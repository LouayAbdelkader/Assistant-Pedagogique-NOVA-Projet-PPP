package com.assistant.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;

/**
 * Utilitaire pour la gestion des tokens JWT
 * Génération, validation et extraction des informations
 */
@Component
public class JwtUtils {

    // Clé secrète pour signer les tokens (à garder secrète en production !)
    private final String jwtSecret = "AssistantPedagogiqueSecretKeyPourLeProjetDeFinDetudes2024TresSecurisee";

    // Durée de validité du token : 24 heures (en millisecondes)
    private final int jwtExpirationMs = 86400000;

    /**
     * Récupère la clé de signature à partir du secret
     * @return Key objet Key pour HMAC-SHA
     */
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    /**
     * Génère un token JWT pour un utilisateur authentifié
     * @param authentication objet d'authentification Spring Security
     * @return token JWT sous forme de chaîne
     */
    public String genererJwtToken(Authentication authentication) {
        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject(userPrincipal.getUsername())  // Email comme sujet
                .setIssuedAt(new Date())                   // Date d'émission
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs)) // Date d'expiration
                .signWith(getSigningKey(), SignatureAlgorithm.HS256) // Signature
                .compact();
    }

    /**
     * Extrait l'email (subject) du token JWT
     * @param token token JWT
     * @return email contenu dans le token
     */
    public String getEmailFromJwtToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /**
     * Valide un token JWT (signature, expiration, format)
     * @param authToken token à valider
     * @return true si le token est valide, false sinon
     */
    public boolean validerJwtToken(String authToken) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(authToken);
            return true;
        } catch (JwtException e) {
            System.err.println("Token Invalide : " + e.getMessage());
        }
        return false;
    }
}