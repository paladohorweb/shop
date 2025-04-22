//package jgm.tiendaVirtual.service;
//
//import jgm.tiendaVirtual.model.Usuario;
//import jgm.tiendaVirtual.security.JwtUtil;
//import jgm.rtiendavirtual.repository.UsuarioRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.web.server.ResponseStatusException;
//
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class AuthServiceTest {
//
//    @Mock
//    private UsuarioRepository usuarioRepository;
//
//    @Mock
//    private JwtUtil jwtUtil;
//
//    @Mock
//    private PasswordEncoder passwordEncoder;
//
//    @InjectMocks
//    private AuthService authService;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void testLogin_Success() {
//        // Datos de prueba
//        String email = "test@email.com";
//        String rawPassword = "password123";
//        String encodedPassword = "$2a$10$D9cA7cD8eXk0u0Jj/BP0Ne2ZayQ3F9HH8RZ1H09ed3fr52zR5wn2K"; // BCrypt de "password123"
//        String token = "fake-jwt-token";
//
//        Usuario usuario = new Usuario();
//        usuario.setEmail(email);
//        usuario.setPassword(encodedPassword);
//
//        // Configurar mocks
//        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.of(usuario));
//        when(passwordEncoder.matches(rawPassword, encodedPassword)).thenReturn(true);
//        when(jwtUtil.generarToken(email)).thenReturn(token);
//
//        // Ejecutar prueba
//        String result = authService.login(email, rawPassword);
//
//        // Verificar resultado
//        assertEquals(token, result);
//    }
//
//    @Test
//    void testLogin_UserNotFound() {
//        String email = "notfound@email.com";
//        String password = "password123";
//
//        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.empty());
//
//        assertThrows(ResponseStatusException.class, () -> authService.login(email, password));
//    }
//
//    @Test
//    void testLogin_InvalidPassword() {
//        String email = "test@email.com";
//        String rawPassword = "wrongpassword";
//        String encodedPassword = "$2a$10$D9cA7cD8eXk0u0Jj/BP0Ne2ZayQ3F9HH8RZ1H09ed3fr52zR5wn2K";
//
//        Usuario usuario = new Usuario();
//        usuario.setEmail(email);
//        usuario.setPassword(encodedPassword);
//
//        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.of(usuario));
//        when(passwordEncoder.matches(rawPassword, encodedPassword)).thenReturn(false);
//
//        assertThrows(ResponseStatusException.class, () -> authService.login(email, rawPassword));
//    }
//
////    //@Test
////    void testRegister_Success() {
////        Usuario usuario = new Usuario();
////        usuario.setEmail("newuser@email.com");
////        usuario.setPassword("plainpassword");
////
////        when(passwordEncoder.encode(usuario.getPassword())).thenReturn("hashedpassword");
////        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);
////
////        Usuario result = authService.re
////
////        assertNotNull(result);
////        assertEquals("newuser@email.com", result.getEmail());
////        assertEquals("hashedpassword", result.getPassword());
//    }
//
//
