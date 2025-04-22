package jgm.tiendaVirtual.service;

/**
 *
 * @author Jorge
 
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import jgm.tiendaVirtual.model.Usuario;

import java.util.List;
import jgm.rtiendavirtual.repository.UsuarioRepository;

@Component
public class PasswordUpdater implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public PasswordUpdater(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        List<Usuario> usuarios = usuarioRepository.findAll();
        for (Usuario usuario : usuarios) {
            String password = usuario.getPassword();
            if (!password.startsWith("$2a$")) { // Verifica si la contraseña ya está encriptada
                usuario.setPassword(passwordEncoder.encode(password)); // Encripta la contraseña
                usuarioRepository.save(usuario);
                System.out.println("Contraseña actualizada para el usuario: " + usuario.getEmail());
            }
        }
    }
}
*/