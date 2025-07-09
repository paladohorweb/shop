package jgm.tiendaVirtual.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "detalles_pedido")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DetallePedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "pedido_id", nullable = false)
    @JsonBackReference // 🔹 Evita la recursión infinita en la serialización
    private Pedido pedido;

    @ManyToOne
    @JoinColumn(name = "producto_id", nullable = false)
    @JsonIgnoreProperties("detalles") // 👈 Evita recursividad si Producto tiene Set<DetallePedido>
    private Producto producto;
     
    
    
    @Positive(message = "La cantidad debe ser mayor a 0")
    private Integer cantidad = 1;
    
    @Positive(message = "El precio unitario debe ser mayor a 0")
    @Column(nullable = false)
    private BigDecimal precioUnitario = BigDecimal.ZERO;

    /** 🔹 Obtiene el subtotal del detalle */
    public BigDecimal getSubtotal() {
        return precioUnitario.multiply(BigDecimal.valueOf(cantidad));
    }
}

