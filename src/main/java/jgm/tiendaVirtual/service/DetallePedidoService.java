package jgm.tiendaVirtual.service;

/**
 *
 * @author Jorge
 */
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import jgm.tiendaVirtual.model.DetallePedido;
import jgm.tiendaVirtual.repository.DetallePedidoRepository;

@Service
public class DetallePedidoService {

    @Autowired
    private DetallePedidoRepository detallePedidoRepository;

    public List<DetallePedido> listarDetalles() {
        return detallePedidoRepository.findAll();
    }
}