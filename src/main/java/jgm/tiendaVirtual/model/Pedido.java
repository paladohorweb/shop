package jgm.tiendaVirtual.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "pedidos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) // 👈 Solo ignoramos lo necesario
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    @JsonIgnoreProperties("pedidos")
    private Usuario usuario;

    private LocalDateTime fechaPedido = LocalDateTime.now(); // ⏳ Fecha automática al crear pedido

    @Enumerated(EnumType.STRING)
    private EstadoPedido estado = EstadoPedido.PENDIENTE; // Estado por defecto

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<DetallePedido> detalles = new HashSet<>();


    @OneToOne(mappedBy = "pedido", cascade = CascadeType.ALL)
    private Factura factura;

    private BigDecimal total; // 💲 Total del pedido

    @Column(name = "metodo_pago")
    private String metodoPago;

    /**
     * 🔹 Método para calcular el total del pedido
     */
    public void calcularTotal() {
        this.total = detalles.stream()
                .map(DetallePedido::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @PrePersist
    @PreUpdate
    public void actualizarTotal() {
        calcularTotal();
    }
}
