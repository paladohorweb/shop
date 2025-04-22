package jgm.tiendaVirtual.repository;

import jgm.tiendaVirtual.model.CarritoItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarritoItemRepository extends JpaRepository<CarritoItem, Long> {
}

