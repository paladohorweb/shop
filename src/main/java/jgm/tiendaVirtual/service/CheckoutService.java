package jgm.tiendaVirtual.service;

import jgm.tiendaVirtual.exception.*;
import jgm.tiendaVirtual.model.*;
import jgm.tiendaVirtual.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class CheckoutService {

    private final PedidoRepository pedidoRepository;
    private final PagoRepository pagoRepository;
    private final ProductoRepository productoRepository;

    @Autowired
    public CheckoutService(
        PedidoRepository pedidoRepository,
        PagoRepository pagoRepository, 
        ProductoRepository productoRepository
    ) {
        this.pedidoRepository = pedidoRepository;
        this.pagoRepository = pagoRepository;
        this.productoRepository = productoRepository;
    }

    @Transactional
    public Pago procesarCheckout(Long pedidoId, MetodoPago metodoPago) throws PedidoInvalidoException, PagoExistenteException, EstadoPedidoException {
        // 1. Validar pedido
        Pedido pedido = pedidoRepository.findById(pedidoId)
            .orElseThrow(() -> new PedidoNotFoundException(pedidoId));

        validarPedido(pedido);

        // 2. Procesar stock
        actualizarStockProductos(pedido);

        // 3. Registrar pago
        Pago pago = crearPago(pedido, metodoPago);

        // 4. Actualizar estado
        actualizarEstadoPedido(pedido);

        return pago;
    }

    private void validarPedido(Pedido pedido) throws PedidoInvalidoException, PagoExistenteException, EstadoPedidoException {
        if (pedido.getDetalles().isEmpty()) {
            throw new PedidoInvalidoException("El pedido no contiene productos");
        }

        if (pagoRepository.existsByPedido(pedido)) {
            throw new PagoExistenteException("El pedido ya tiene un pago registrado");
        }

        if (pedido.getEstado() != EstadoPedido.PENDIENTE) {
            throw new EstadoPedidoException("Solo se pueden pagar pedidos en estado PENDIENTE");
        }
    }

    private void actualizarStockProductos(Pedido pedido) {
        pedido.getDetalles().forEach(detalle -> {
            Producto producto = detalle.getProducto();
            if (producto.getStock() < detalle.getCantidad()) {
                throw new StockInsuficienteException(
                    "Stock insuficiente para: " + producto.getNombre() + 
                    " (Disponible: " + producto.getStock() + ")"
                );
            }
            producto.setStock(producto.getStock() - detalle.getCantidad());
            productoRepository.save(producto);
        });
    }

    private Pago crearPago(Pedido pedido, MetodoPago metodoPago) {
        Pago pago = new Pago();
        pago.setPedido(pedido);
        pago.setMetodoPago(metodoPago);
        pago.setMonto(pedido.getTotal());
        pago.setEstado(EstadoPago.CONFIRMADO);
        return pagoRepository.save(pago);
    }

    private void actualizarEstadoPedido(Pedido pedido) {
        pedido.setEstado(EstadoPedido.PAGADO);
        pedidoRepository.save(pedido);
    }

    private static class EstadoPedidoException extends Exception {

        public EstadoPedidoException(String solo_se_pueden_pagar_pedidos_en_estado_PE) {
        }
    }

    private static class PedidoInvalidoException extends Exception {

        public PedidoInvalidoException(String el_pedido_no_contiene_productos) {
        }
    }

    private static class PagoExistenteException extends Exception {

        public PagoExistenteException(String el_pedido_ya_tiene_un_pago_registrado) {
        }
    }
}