package jgm.tiendaVirtual.dto;

import java.util.ArrayList;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PedidoDTO {
    private Long usuarioId;
     private List<DetallePedidoDTO> detalles = new ArrayList<>(); // ✅ Evita valores nulos
}



