package jgm.tiendaVirtual.service;

import jgm.tiendaVirtual.model.Usuario;
import jgm.tiendaVirtual.security.JwtUtil;
import jgm.tiendaVirtual.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import jgm.tiendaVirtual.dto.UsuarioDTO;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UsuarioRepository usuarioRepository, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    /** 🔹 Autenticación y generación de JWT */
@Transactional(readOnly = true)
public Map<String, Object> login(String email, String password) {
    Usuario usuario = usuarioRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("❌ Usuario o contraseña incorrectos"));

    if (!passwordEncoder.matches(password, usuario.getPassword())) {
        throw new UsernameNotFoundException("❌ Usuario o contraseña incorrectos"); // 🔹 Evita dar pistas al atacante
    }

    List<String> roles = Collections.singletonList(usuario.getRol().name()); // ✅ Obtiene rol como String
    String token = jwtUtil.generarToken(usuario.getId(), email, roles); // ✅ Ahora pasa userId al token

    System.out.println("🔹 Token generado: " + token);

    // ✅ Devuelve también el ID del usuario (útil en frontend)
  UsuarioDTO usuarioDTO = new UsuarioDTO(usuario.getId(), usuario.getEmail(), usuario.getRol());

return Map.of(
    "token", token,
    "usuarioId", usuario.getId(),
    "usuario", usuarioDTO, // ✅ Agregamos el objeto usuario
    "roles", roles
);
}
}







