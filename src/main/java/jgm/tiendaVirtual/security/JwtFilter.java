package jgm.tiendaVirtual.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import jgm.tiendaVirtual.service.CustomUserDetailsService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.logging.Logger;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;
    private static final Logger logger = Logger.getLogger(JwtFilter.class.getName());

    public JwtFilter(JwtUtil jwtUtil, CustomUserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            try {
                if (!jwtUtil.validarToken(token)) {
                    logger.warning("❌ Token inválido.");
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Acceso no autorizado.");
                    return;
                }

                String email = jwtUtil.extraerEmail(token);
                if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    // 🔽 Cargar el UserDetails completo desde DB
                    UserDetails userDetails = userDetailsService.loadUserByUsername(email);

                    if (jwtUtil.validarToken(token)) {
                        UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(
                                        userDetails,
                                        null,
                                        userDetails.getAuthorities()
                                );

                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        logger.info("✅ Usuario autenticado: " + email);
                    }
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
