package jgm.tiendaVirtual.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.springframework.security.core.context.SecurityContext;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private static final Logger logger = Logger.getLogger(JwtFilter.class.getName());

    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            try {
                // ✅ Validar el token antes de continuar
                if (!jwtUtil.validarToken(token)) {
                    logger.warning("❌ Token inválido.");
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Acceso no autorizado.");
                    return;
                }

                String email = jwtUtil.extraerEmail(token);
                List<String> roles = jwtUtil.extraerRoles(token);

                if (email != null && !roles.isEmpty()) {
                    // 🔹 Asegurar que los roles incluyen "ROLE_"
                    List<SimpleGrantedAuthority> authorities = roles.stream()
                            .map(role -> new SimpleGrantedAuthority(role.startsWith("ROLE_") ? role : "ROLE_" + role))
                            .collect(Collectors.toList());

                    // Crear la autenticación en el contexto de seguridad
                    UsernamePasswordAuthenticationToken authToken
                            = new UsernamePasswordAuthenticationToken(email, null, authorities);
                    SecurityContext context = SecurityContextHolder.createEmptyContext();
                    context.setAuthentication(authToken);
                    SecurityContextHolder.setContext(context);
                    logger.info("✅ Usuario autenticado: " + email);
                    logger.info("✅ Roles asignados: " + authorities);
                } else {
                    logger.warning("⚠️ Token válido pero sin información de usuario.");
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Acceso no autorizado.");
                    return;
                }

            } catch (Exception e) {
                logger.warning("❌ Error al validar el token: " + e.getMessage());
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Acceso no autorizado.");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
