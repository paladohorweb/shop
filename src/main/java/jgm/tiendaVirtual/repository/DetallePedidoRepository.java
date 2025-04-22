package jgm.tiendaVirtual.repository;

import jgm.tiendaVirtual.model.DetallePedido;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Jorge
 */
public interface DetallePedidoRepository extends JpaRepository<DetallePedido, Long> {
    
}
