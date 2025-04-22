package jgm.tiendaVirtual.repository;

import jgm.tiendaVirtual.model.Pago;
import jgm.tiendaVirtual.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Long> {
    Optional<Pago> findByPedido(Pedido pedido);

    public boolean existsByPedido(Pedido pedido);
}

