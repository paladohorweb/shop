package jgm.tiendaVirtual.dto;

import jgm.tiendaVirtual.model.Rol;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UsuarioDTO {
    private Long id;
    private String email;
    private String nombre;
    private Rol rol;
}

