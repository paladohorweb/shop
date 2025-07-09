package jgm.tiendaVirtual.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "facturas")
public class Factura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String numero;

    private LocalDateTime fechaEmision;



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    private BigDecimal total;

    private BigDecimal iva;

    @Column(name = "metodo_pago")
    private String metodoPago;

    @OneToMany(mappedBy = "factura", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference // 👈 Controla el lado "padre" de la relación
    private List<DetalleFactura> detalles;

    @OneToOne
    @JoinColumn(name = "pedido_id")
    @JsonIgnoreProperties("factura") // 👈 evita recursión inversa si se usa Pedido aquí
    private Pedido pedido;


}

