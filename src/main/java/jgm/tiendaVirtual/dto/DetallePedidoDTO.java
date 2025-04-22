package jgm.tiendaVirtual.dto;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DetallePedidoDTO {
    private Long productoId;
    private Integer cantidad;
    private BigDecimal precioUnitario;
  
 
}

