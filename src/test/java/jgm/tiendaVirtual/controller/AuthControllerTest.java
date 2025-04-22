//package jgm.tiendaVirtual.controller;
//
//import jgm.tiendaVirtual.service.AuthService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//
//import java.util.Map;
//
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//class AuthControllerTest {
//
//    private MockMvc mockMvc;
//
//    @Mock
//    private AuthService authService;
//
//    @InjectMocks
//    private AuthController authController;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
//    }
//
//    @Test
//    void testLogin_Success() throws Exception {
//        String email = "test@email.com";
//        String password = "password123";
//        String token = "fake-jwt-token";
//
//        when(authService.login(email, password)).thenReturn(token);
//
//        mockMvc.perform(post("/api/auth/login")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content("{\"email\":\"test@email.com\", \"password\":\"password123\"}"))
//                .andExpect(status().isOk())
//                .andExpect(content().json("{\"token\":\"fake-jwt-token\"}"));
//    }
//
//    @Test
//    void testLogin_InvalidCredentials() throws Exception {
//        when(authService.login("wrong@email.com", "wrongpassword"))
//                .thenThrow(new RuntimeException("Credenciales inválidas"));
//
//        mockMvc.perform(post("/api/auth/login")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content("{\"email\":\"wrong@email.com\", \"password\":\"wrongpassword\"}"))
//                .andExpect(status().isInternalServerError());
//    }
//}
//
