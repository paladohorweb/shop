package jgm.tiendaVirtual.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Credito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal montoSolicitado;

    private Integer cuotas;

    private BigDecimal interes;

    private BigDecimal montoFinal;

    private LocalDateTime fechaSolicitud;

    private LocalDateTime fechaAprobacion;

    @Enumerated(EnumType.STRING)
    private EstadoCredito estado;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "producto_id")
    private Producto producto;

    @ManyToOne
    @JoinColumn(name = "pedido_id")
    private Pedido pedido;

    public enum EstadoCredito {
        PENDIENTE,
        APROBADO,
        RECHAZADO
    }
}
