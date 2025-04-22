package jgm.tiendaVirtual.service;

import jgm.tiendaVirtual.dto.PagoDTO;
import jgm.tiendaVirtual.model.*;
import jgm.tiendaVirtual.repository.PagoRepository;
import jgm.tiendaVirtual.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.time.LocalDateTime;

@Service
public class PagoService {

    @Autowired
    private PagoRepository pagoRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    /**
     * 🔹 Método para procesar un pago
     * @param pedidoId
     * @param metodoPago
     * @return 
     */
@Transactional
public PagoDTO procesarPago(Long pedidoId, MetodoPago metodoPago) {
    Pedido pedido = pedidoRepository.findById(pedidoId)
            .orElseThrow(() -> new IllegalArgumentException("Pedido no encontrado"));

    if (pagoRepository.existsByPedido(pedido)) {
        throw new IllegalStateException("El pedido ya tiene un pago registrado");
    }

    // 🔹 Crear el pago correctamente con el constructor
    Pago pago = new Pago();
    pago.setPedido(pedido);
    pago.setMetodoPago(metodoPago);
    pago.setMonto(pedido.getTotal());

    // 🔹 Simulación de fallo (20% de los pagos fallan)
    boolean pagoExitoso = Math.random() > 0.2;

    if (pagoExitoso) {
        pago.setEstado(EstadoPago.COMPLETADO);
        pago.setFechaPago(LocalDateTime.now());
        pedido.setEstado(EstadoPedido.PAGADO);
    } else {
        pago.setEstado(EstadoPago.RECHAZADO);
    }

    // 🔹 Guardar los cambios correctamente
    pago = pagoRepository.save(pago);
    pedidoRepository.save(pedido);

    return convertirPagoADTO(pago);
}
 





/**
     * 🔹 Método para actualizar el estado de un pago
     * @param pagoId
     * @param estadoPago
     * @return 
     */
    @Transactional
    public PagoDTO actualizarEstadoPago(Long pagoId, EstadoPago estadoPago) {
        Pago pago = pagoRepository.findById(pagoId)
                .orElseThrow(() -> new IllegalArgumentException("Pago no encontrado"));

        pago.setEstado(estadoPago);
        pagoRepository.save(pago);

        return convertirPagoADTO(pago);
    }

    /**
     * 🔹 Método para convertir Pago -> PagoDTO
     */
    private PagoDTO convertirPagoADTO(Pago pago) {
        PagoDTO dto = new PagoDTO();
        dto.setId(pago.getId());
        dto.setPedidoId(pago.getPedido().getId());
        dto.setMetodoPago(pago.getMetodoPago());
        dto.setMonto(pago.getMonto());
        dto.setFechaPago(pago.getFechaPago());
        dto.setEstado(pago.getEstado());
        return dto;
    }
}
