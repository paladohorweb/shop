package jgm.tiendaVirtual.repository;

import java.util.List;
import jgm.tiendaVirtual.model.Pedido;
import jgm.tiendaVirtual.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Jorge
 */

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByUsuario(Usuario usuario);
}
