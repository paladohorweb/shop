package jgm.tiendaVirtual.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FacturaDTO {


    private Long id;

    private String numero;

    private LocalDateTime fechaEmision;

    private String cliente;

    private BigDecimal total;

    private BigDecimal iva;


    private String metodoPago;

    private List<DetalleFacturaDTO> detalles;
}

