package jgm.tiendaVirtual.controller;

import jgm.tiendaVirtual.dto.UsuarioAdminDTO;
import jgm.tiendaVirtual.model.Usuario;
import jgm.tiendaVirtual.model.Rol;
import jgm.tiendaVirtual.repository.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin/usuarios")
@CrossOrigin(origins = "http://localhost:4200")
@PreAuthorize("hasRole('ADMIN')")
public class AdminUsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepo;

    @GetMapping
    public List<UsuarioAdminDTO> listarUsuarios() {
        return usuarioRepo.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @PutMapping("/{id}")
    public UsuarioAdminDTO actualizarUsuario(@PathVariable Long id, @RequestBody UsuarioAdminDTO dto) {
        Usuario usuario = usuarioRepo.findById(id).orElseThrow();
        usuario.setNombre(dto.getNombre());
        usuario.setEmail(dto.getEmail());
        usuario.setRol(dto.getRol()); // Rol es Enum, no String
        usuarioRepo.save(usuario);
        return mapToDto(usuario);
    }

    private UsuarioAdminDTO mapToDto(Usuario u) {
        UsuarioAdminDTO dto = new UsuarioAdminDTO();
        dto.setId(u.getId());
        dto.setNombre(u.getNombre());
        dto.setEmail(u.getEmail());
        dto.setRol(u.getRol());
        return dto;
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        Usuario usuario = usuarioRepo.findById(id).orElse(null);
        if (usuario == null) {
            return ResponseEntity.notFound().build();
        }

        // No permitir que se borre a sí mismo
        if (usuario.getEmail().equalsIgnoreCase(userDetails.getUsername())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        usuarioRepo.delete(usuario);
        return ResponseEntity.noContent().build();
    }
}
