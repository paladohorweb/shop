package jgm.tiendaVirtual.repository;

import jgm.tiendaVirtual.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *
 * @author Jorge
 */

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    List<Producto> findByDestacadoTrue();// ✅ Devuelve productos donde destacado = true

    List<Producto> findTop10ByOrderByCantidadVendidaDesc();
}
