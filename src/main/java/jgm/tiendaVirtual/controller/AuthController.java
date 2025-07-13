package jgm.tiendaVirtual.controller;

import jakarta.validation.Valid;
import jgm.tiendaVirtual.dto.RegisterRequest;
import jgm.tiendaVirtual.dto.UsuarioDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import jgm.tiendaVirtual.dto.LoginRequest;
import jgm.tiendaVirtual.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200") // Permitir Angular
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        System.out.println("Intentando iniciar sesión con: " + loginRequest); // Debugging

        try {
            // Llamamos al servicio para autenticar y generar el token
            Map<String, Object> authResponse = authService.login(loginRequest.getEmail(), loginRequest.getPassword());

            return ResponseEntity.ok(authResponse);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Credenciales incorrectas"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Error en la solicitud"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Error del servidor: " + e.getMessage()));
        }
    }



    @PostMapping("/register")
    public ResponseEntity<?> registrar(@Valid @RequestBody RegisterRequest request) {
        try {
            UsuarioDTO nuevoUsuario = authService.registrarUsuario(request);
            return ResponseEntity.ok(nuevoUsuario);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Error interno del servidor"));
        }
    }
}



