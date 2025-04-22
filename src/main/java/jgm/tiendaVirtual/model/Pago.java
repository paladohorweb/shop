package jgm.tiendaVirtual.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Data
@Entity
@Table(name = "pagos")
@NoArgsConstructor
@AllArgsConstructor
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "pedido_id", nullable = false, unique = true)
    private Pedido pedido;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MetodoPago metodoPago;

    @Column(nullable = false)
    private BigDecimal monto;

    private LocalDateTime fechaPago;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoPago estado = EstadoPago.PENDIENTE; // Estado inicial del pago

    /** ✅ Constructor válido
     * @param pedido
     * @param metodoPago
     * @param monto */
    public Pago(Pedido pedido, MetodoPago metodoPago, BigDecimal monto) {
        this.pedido = pedido;
        this.metodoPago = metodoPago;
        this.monto = monto;
        this.estado = EstadoPago.PENDIENTE; // Estado por defecto
    }

    /** ✅ Método para confirmar el pago */
    public void confirmarPago() {
        this.estado = EstadoPago.COMPLETADO;
        this.fechaPago = LocalDateTime.now();
    }

    /** ✅ equals() y hashCode() para evitar errores en colecciones */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pago pago = (Pago) o;
        return Objects.equals(id, pago.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}



