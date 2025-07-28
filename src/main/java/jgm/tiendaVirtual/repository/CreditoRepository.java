package jgm.tiendaVirtual.repository;

import jgm.tiendaVirtual.model.Credito;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CreditoRepository extends JpaRepository<Credito, Long> {
    List<Credito> findByUsuarioId(Long usuarioId);
}
