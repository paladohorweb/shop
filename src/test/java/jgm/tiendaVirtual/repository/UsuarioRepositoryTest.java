//package jgm.tiendaVirtual.repository;
//
//import jgm.tiendaVirtual.model.Usuario;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//
//import java.util.Optional;
//import jgm.tiendaVirtual.model.Rol;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@ExtendWith(SpringExtension.class)
//@DataJpaTest
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY) // 🔹 Asegura que use H2 en memoria
//class UsuarioRepositoryTest {
//
//    @Autowired
//    private UsuarioRepository usuarioRepository;
//
//@Test
//void testFindByEmail() {
//    Usuario usuario = new Usuario();
//    usuario.setEmail("test@email.com");
//    usuario.setPassword("password123");
//    usuario.setRol(Rol.USER); // 🔹 Agregar un rol antes de guardar
//
//    usuarioRepository.save(usuario);
//
//    Optional<Usuario> foundUser = usuarioRepository.findByEmail("test@email.com");
//    assertTrue(foundUser.isPresent());
//    assertEquals("test@email.com", foundUser.get().getEmail());
//}
//}
//

