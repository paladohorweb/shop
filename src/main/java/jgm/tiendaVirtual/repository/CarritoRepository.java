package jgm.tiendaVirtual.repository;

import jgm.tiendaVirtual.model.Carrito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import jgm.tiendaVirtual.model.Usuario;

@Repository
public interface CarritoRepository extends JpaRepository<Carrito, Long> {
    Optional<Carrito> findByUsuarioId(Long usuarioId);

      public Object findByUsuario(Usuario usuario);
    
   
}


