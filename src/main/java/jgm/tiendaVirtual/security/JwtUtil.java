package jgm.tiendaVirtual.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Component
public class JwtUtil {

    private static final Logger logger = Logger.getLogger(JwtUtil.class.getName());

    private final SecretKey key;

    // Constructor para inyectar la clave desde application.properties
    public JwtUtil(@Value("${security.jwt.secret}") String secret) {
        this.key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secret));
    }

    /**
     * Genera un token JWT con email y roles.
     */
public String generarToken(Long userId, String email, List<String> roles) {
    // 🔹 Asegurar que los roles incluyen "ROLE_"
    List<String> rolesConPrefijo = roles.stream()
            .map(role -> role.startsWith("ROLE_") ? role : "ROLE_" + role)
            .collect(Collectors.toList());

    return Jwts.builder()
            .setSubject(email)
            .claim("userId", userId) // ✅ Agregar ID del usuario al token
            .claim("roles", rolesConPrefijo)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 horas
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();
}

    /**
     * Extrae el email desde el token.
     */
    public String extraerEmail(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (ExpiredJwtException e) {
            logger.warning("⚠️ Token expirado");
        } catch (SignatureException e) {
            logger.warning("⚠️ Firma del token no válida");
        } catch (MalformedJwtException e) {
            logger.warning("⚠️ Token mal formado");
        } catch (JwtException | IllegalArgumentException e) {
            logger.warning("⚠️ Token inválido");
        }
        return null;
    }

    /**
     * Extrae los roles desde el token.
     */
    public List<String> extraerRoles(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            Object rolesObj = claims.get("roles");

            if (rolesObj instanceof List<?>) {
                return ((List<?>) rolesObj).stream()
                        .map(Object::toString)
                        .collect(Collectors.toList());
            }
        } catch (Exception e) {
            logger.warning("⚠️ No se pudieron extraer roles del token.");
        }
        return List.of(); // Retornar lista vacía en caso de error
    }

    /**
     * Valida si el token es válido y no ha expirado.
     */
    public boolean validarToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
            logger.warning("❌ Token expirado");
        } catch (SignatureException e) {
            logger.warning("❌ Firma del token no válida");
        } catch (MalformedJwtException e) {
            logger.warning("❌ Token mal formado");
        } catch (JwtException | IllegalArgumentException e) {
            logger.warning("❌ Token inválido");
        }
        return false;
    }
}


