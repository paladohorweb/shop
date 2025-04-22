package jgm.tiendaVirtual.repository;

import jgm.tiendaVirtual.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Jorge
 */

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    
}
