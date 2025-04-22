//package jgm.tiendaVirtual.security;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.util.Date;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class JwtUtilTest {
//
//    private JwtUtil jwtUtil;
//    private final String secretKey = "secret"; // Debe coincidir con el que usas en `JwtUtil`
//
//    @BeforeEach
//    void setUp() {
//        jwtUtil = new JwtUtil();
//    }
//
//    @Test
//    void testGenerateToken() {
//        String email = "test@email.com";
//        String token = jwtUtil.generarToken(email);
//        assertNotNull(token);
//    }
//
//    @Test
//    void testValidateToken() {
//        String email = "test@email.com";
//        String token = jwtUtil.generarToken(email);
//        assertTrue(jwtUtil.validarToken(token));
//    }
//
//    @Test
//    void testExtractEmail() {
//        String email = "test@email.com";
//        String token = jwtUtil.generarToken(email);
//        String extractedEmail = jwtUtil.extraerEmail(token);
//        assertEquals(email, extractedEmail);
//    }
//}
//
