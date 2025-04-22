package jgm.tiendaVirtual.dto;

import jgm.tiendaVirtual.model.EstadoPago;
import jgm.tiendaVirtual.model.MetodoPago;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PagoDTO {

    private Long id;  // ID del pago
    private Long pedidoId;  // ID del pedido asociado
    private MetodoPago metodoPago;  // Método de pago usado
    private BigDecimal monto;  // Monto total del pago
    private LocalDateTime fechaPago;  // Fecha en que se realizó el pago
    private EstadoPago estado;  // Estado actual del pago (PENDIENTE, COMPLETADO, FALLIDO)

}

