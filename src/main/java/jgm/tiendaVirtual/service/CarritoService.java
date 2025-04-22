package jgm.tiendaVirtual.service;

import jgm.tiendaVirtual.dto.CarritoDTO;
import jgm.tiendaVirtual.dto.CarritoItemDTO;
import jgm.tiendaVirtual.model.*;
import jgm.tiendaVirtual.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CarritoService {

    @Autowired
    private CarritoRepository carritoRepository;

    @Autowired
    private CarritoItemRepository carritoItemRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private PagoRepository pagoRepository;

    /** 🔹 Obtener el carrito de un usuario
     * @param usuarioId
     * @return  */
    public CarritoDTO obtenerCarrito(Long usuarioId) {
        Carrito carrito = obtenerCarritoEntidad(usuarioId);
        return convertirACarritoDTO(carrito);
    }

    /** 🔹 Agregar un producto al carrito
     * @param usuarioId
     * @param productoId
     * @param cantidad
     * @return  */
    @Transactional
    public CarritoDTO agregarProducto(Long usuarioId, Long productoId, int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a 0");
        }

        Carrito carrito = obtenerCarritoEntidad(usuarioId);
        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));

        Optional<CarritoItem> itemExistente;
        itemExistente = carrito.getItems().stream()
                .filter(item -> item.getProducto().getId().equals(productoId))
                .findFirst();

        if (itemExistente.isPresent()) {
            CarritoItem item = itemExistente.get();
            item.setCantidad(item.getCantidad() + cantidad);
        } else {
            CarritoItem nuevoItem = new CarritoItem();
            nuevoItem.setCarrito(carrito);
            nuevoItem.setProducto(producto);
            nuevoItem.setCantidad(cantidad);
           nuevoItem.setPrecioUnitario(producto.getPrecio());
            carrito.getItems().add(nuevoItem);
        }

        calcularTotal(carrito);
        return convertirACarritoDTO(carrito);
    }

    /** 🔹 Eliminar un producto del carrito
     * @param usuarioId
     * @param productoId
     * @return  */
    @Transactional
    public CarritoDTO eliminarProducto(Long usuarioId, Long productoId) {
        Carrito carrito = obtenerCarritoEntidad(usuarioId);
        carrito.getItems().removeIf(item -> item.getProducto().getId().equals(productoId));

        calcularTotal(carrito);
        return convertirACarritoDTO(carrito);
    }

    /** 🔹 Vaciar el carrito
     * @param usuarioId
     * @return  */
    @Transactional
    public CarritoDTO vaciarCarrito(Long usuarioId) {
        Carrito carrito = obtenerCarritoEntidad(usuarioId);
        carrito.getItems().clear();
        calcularTotal(carrito);
        return convertirACarritoDTO(carrito);
    }

    /** 🔹 Actualizar cantidad de un producto en el carrito
     * @param usuarioId
     * @param itemId
     * @param cantidad
     * @return  */
    @Transactional
    public CarritoDTO actualizarCantidad(Long usuarioId, Long itemId, int cantidad) {
        if (cantidad <= 0) {
            return eliminarProducto(usuarioId, itemId);
        }

        Carrito carrito = obtenerCarritoEntidad(usuarioId);

        CarritoItem item = carrito.getItems().stream()
                .filter(i -> i.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("El producto no está en el carrito"));

        item.setCantidad(cantidad);
        calcularTotal(carrito);
        return convertirACarritoDTO(carrito);
    }

    /** 🔹 Procesar checkout
     * @param usuarioId
     * @param metodoPago
     * @return  */
    @Transactional
    public Pedido procesarCheckout(Long usuarioId, MetodoPago metodoPago) {
        Carrito carrito = obtenerCarritoEntidad(usuarioId);

        if (carrito.getItems().isEmpty()) {
            throw new IllegalStateException("El carrito está vacío");
        }

        Pedido pedido = new Pedido();
        pedido.setUsuario(carrito.getUsuario());
        pedido.setEstado(EstadoPedido.PENDIENTE);
        pedido.setTotal(BigDecimal.ZERO); // Se calculará después

        // Guardar el pedido inicial para obtener el ID
        pedido = pedidoRepository.save(pedido);

        BigDecimal totalPedido = BigDecimal.ZERO;

        for (CarritoItem item : carrito.getItems()) {
            DetallePedido detalle = new DetallePedido();
            detalle.setPedido(pedido);
            detalle.setProducto(item.getProducto());
            detalle.setCantidad(item.getCantidad());
            detalle.setPrecioUnitario(item.getPrecioUnitario());

            totalPedido = totalPedido.add(detalle.getSubtotal());
            pedido.getDetalles().add(detalle);
        }

        // Asignar el total calculado y guardar el pedido actualizado
        pedido.setTotal(totalPedido);
        pedidoRepository.save(pedido);

        // Registrar el pago
        Pago pago = new Pago(pedido, metodoPago, pedido.getTotal());
        pago.setPedido(pedido);
        pago.setMetodoPago(metodoPago);
        pago.setMonto(pedido.getTotal());
        pago.setEstado(EstadoPago.PENDIENTE);
        pagoRepository.save(pago);

        // Vaciar el carrito
        carrito.getItems().clear();
        carrito.setTotal(BigDecimal.ZERO);
        carritoRepository.save(carrito);

        return pedido;
    }

    /** 🔹 Calcular el total del carrito */
    private void calcularTotal(Carrito carrito) {
        BigDecimal total = carrito.getItems().stream()
                .map(item -> item.getPrecioUnitario().multiply(BigDecimal.valueOf(item.getCantidad())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        carrito.setTotal(total);
    }

    /** 🔹 Obtener o crear carrito del usuario */
private Carrito obtenerCarritoEntidad(Long usuarioId) {
    Usuario usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

    Optional<Carrito> carritoOpt = (Optional<Carrito>) carritoRepository.findByUsuario(usuario);

    if (carritoOpt.isPresent()) {
        return carritoOpt.get();
    } else {
        Carrito nuevoCarrito = new Carrito();
        nuevoCarrito.setUsuario(usuario);
        nuevoCarrito.setTotal(BigDecimal.ZERO);
        return carritoRepository.save(nuevoCarrito); // 👈 Guardamos y retornamos
    }
}

    /** 🔹 Convertir Carrito a DTO */
    private CarritoDTO convertirACarritoDTO(Carrito carrito) {
        List<CarritoItemDTO> itemsDTO = carrito.getItems().stream()
                .map(item -> new CarritoItemDTO(
                        item.getProducto().getId(),
                        item.getProducto().getNombre(),
                        item.getCantidad(),
                        item.getPrecioUnitario()))
                .collect(Collectors.toList());

        return new CarritoDTO(carrito.getUsuario().getId(), carrito.getTotal(), itemsDTO);
    }
}



