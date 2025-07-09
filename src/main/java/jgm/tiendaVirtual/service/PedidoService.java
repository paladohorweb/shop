package jgm.tiendaVirtual.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import jgm.tiendaVirtual.dto.DetallePedidoDTO;
import jgm.tiendaVirtual.model.DetallePedido;
import jgm.tiendaVirtual.model.Pedido;
import jgm.tiendaVirtual.model.Producto;
import jgm.tiendaVirtual.model.Usuario;
import jgm.tiendaVirtual.repository.PedidoRepository;
import jgm.tiendaVirtual.repository.ProductoRepository;
import jgm.tiendaVirtual.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import jgm.tiendaVirtual.model.EstadoPago;
import jgm.tiendaVirtual.model.EstadoPedido;
import jgm.tiendaVirtual.model.MetodoPago;
import jgm.tiendaVirtual.model.Pago;
import jgm.tiendaVirtual.repository.PagoRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private PagoRepository pagoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ProductoRepository productoRepository;

    /**
     * 🔹 Crear un nuevo pedido
     *
     * @param usuarioId
     * @param detallesDTO
     * @return
     */
  @Transactional
    public Pedido crearPedido(Long usuarioId, List<DetallePedidoDTO> detallesDTO) {
        // 1. Validación inicial
        if (usuarioId == null || detallesDTO == null || detallesDTO.isEmpty()) {
            throw new IllegalArgumentException("Datos de pedido inválidos");
        }

        // 2. Obtener usuario
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        // 3. Crear pedido base
        Pedido pedido = new Pedido();
        pedido.setUsuario(usuario);
        pedido.setFechaPedido(LocalDateTime.now());
        pedido.setEstado(EstadoPedido.PENDIENTE);

        // 4. Procesar detalles
        Set<DetallePedido> detalles = new HashSet<>();
        BigDecimal totalPedido = BigDecimal.ZERO;

        for (DetallePedidoDTO detalleDTO : detallesDTO) {
            Producto producto = productoRepository.findById(detalleDTO.getProductoId())
                    .orElseThrow(() -> new IllegalArgumentException(
                        String.format("Producto ID %d no encontrado", detalleDTO.getProductoId()))
                    );

            // Validar stock
            if (producto.getStock() < detalleDTO.getCantidad()) {
                throw new IllegalArgumentException(
                    String.format("Stock insuficiente para %s (Disponible: %d)", 
                    producto.getNombre(), producto.getStock())
                );
            }

            // Actualizar stock
            producto.setStock(producto.getStock() - detalleDTO.getCantidad());
            productoRepository.save(producto);

            // Crear detalle
            DetallePedido detalle = new DetallePedido();
            detalle.setPedido(pedido);
            detalle.setProducto(producto);
            detalle.setCantidad(detalleDTO.getCantidad());
            detalle.setPrecioUnitario(producto.getPrecio().setScale(2, RoundingMode.HALF_UP));
            
            detalles.add(detalle);
            totalPedido = totalPedido.add(detalle.getPrecioUnitario()
                .multiply(BigDecimal.valueOf(detalle.getCantidad())));
        }

        // 5. Finalizar pedido
        pedido.setDetalles(detalles);
        pedido.setTotal(totalPedido.setScale(2, RoundingMode.HALF_UP));
        
        return pedidoRepository.save(pedido);
    }

    @Transactional
    public List<Pedido> obtenerPedidosPorUsuario(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        return pedidoRepository.findByUsuario(usuario);
    }

    @Transactional
    public List<Pedido> listarPedidos() {
        return pedidoRepository.findAll();
    }

    @Transactional
    public void actualizarEstado(Long idPedido, String nuevoEstado) {
        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new IllegalArgumentException("Pedido no encontrado"));

        try {
            EstadoPedido estadoEnum = EstadoPedido.valueOf(nuevoEstado.toUpperCase()); // Convierte el estado a Enum
            pedido.setEstado(estadoEnum); // Asigna el nuevo estado
            pedidoRepository.save(pedido); // Guarda los cambios
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Estado no válido: " + nuevoEstado);
        }
    }

    @Transactional
    public void procesarPago(Long idPedido, String metodoPago) {
        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new IllegalArgumentException("Pedido no encontrado"));

        if (!pedido.getEstado().equals(EstadoPedido.PENDIENTE)) {
            throw new IllegalStateException("El pedido ya ha sido pagado o cancelado.");
        }

        // 🔹 Luego actualizamos el estado del pedido
        pedido.setEstado(EstadoPedido.PAGADO);
        pedidoRepository.save(pedido);

        // 🔹 Crear y guardar el pago antes de actualizar el estado del pedido
        Pago pago = new Pago();
        pago.setPedido(pedido);
        pago.setMetodoPago(MetodoPago.valueOf(metodoPago.toUpperCase())); // Convierte el string a enum
        pago.setMonto(pedido.getTotal());
        pago.setFechaPago(LocalDateTime.now());
        pago.setEstado(EstadoPago.COMPLETADO);

        // 🔹 Guardamos el pago primero
        System.out.println("✅ Guardando pago en la base de datos...");
        pagoRepository.save(pago);
        System.out.println("✅ Pago guardado correctamente.");

    }
    
    
    @Transactional
    public Optional<Pedido> obtenerPedidoPorId(Long idPedido) {
    return pedidoRepository.findById(idPedido);
}

    @Transactional
    public void pagarPedido(Long pedidoId) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        if (!pedido.getEstado().equals(EstadoPedido.PENDIENTE)) {
            throw new IllegalStateException("Solo se pueden pagar pedidos pendientes");
        }

        pedido.setEstado(EstadoPedido.PAGADO);
        pedidoRepository.save(pedido);
    }


    @Transactional
    public void cancelarPedido(Long pedidoId) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        if (!pedido.getEstado().equals(EstadoPedido.PENDIENTE)) {
            throw new IllegalStateException("Solo se pueden cancelar pedidos pendientes");
        }

        pedido.setEstado(EstadoPedido.CANCELADO);
        pedidoRepository.save(pedido);
    }
}

