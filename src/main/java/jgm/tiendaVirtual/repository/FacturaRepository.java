package jgm.tiendaVirtual.repository;

import jgm.tiendaVirtual.model.Factura;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FacturaRepository extends JpaRepository<Factura, Long> {
    Optional<Factura> findByPedidoId(Long pedidoId);
}

