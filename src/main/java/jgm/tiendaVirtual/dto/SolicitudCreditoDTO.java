package jgm.tiendaVirtual.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SolicitudCreditoDTO {
    private Long productoId;
    private BigDecimal montoSolicitado;
    private Integer cuotas;
   // private Long pedidoId;
}
