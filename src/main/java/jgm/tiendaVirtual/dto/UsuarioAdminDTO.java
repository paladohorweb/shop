package jgm.tiendaVirtual.dto;

import jgm.tiendaVirtual.model.Rol;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UsuarioAdminDTO {
    private Long id;
    private String nombre;
    private String email;
    private Rol rol; // Enum: ROLE_USER o ROLE_ADMIN
}
