package jgm.tiendaVirtual.service;

import jgm.tiendaVirtual.dto.DetalleFacturaDTO;
import jgm.tiendaVirtual.dto.FacturaDTO;
import jgm.tiendaVirtual.model.DetalleFactura;
import jgm.tiendaVirtual.model.Factura;
import jgm.tiendaVirtual.model.Pedido;
import jgm.tiendaVirtual.model.Producto;
import jgm.tiendaVirtual.repository.FacturaRepository;
import jgm.tiendaVirtual.repository.PedidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FacturaService {

    private final PedidoRepository pedidoRepository;
    private final FacturaRepository facturaRepository;

    public FacturaDTO generarFactura(Long pedidoId) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        // Crear factura
        Factura factura = new Factura();
        factura.setNumero("FAC-" + System.currentTimeMillis());
        factura.setFechaEmision(LocalDateTime.now());
        factura.setPedido(pedido);
        factura.setUsuario(pedido.getUsuario());
        factura.setMetodoPago(pedido.getMetodoPago());

        // Detalles
        List<DetalleFactura> detalles = pedido.getDetalles().stream().map(detallePedido -> {
            Producto producto = detallePedido.getProducto();
            DetalleFactura detalle = new DetalleFactura();
            detalle.setProductoNombre(producto.getNombre());
            detalle.setCantidad(detallePedido.getCantidad());
            detalle.setPrecioUnitario(producto.getPrecio());
            detalle.setSubtotal(producto.getPrecio().multiply(BigDecimal.valueOf(detallePedido.getCantidad())));
            detalle.setFactura(factura);
            return detalle;
        }).collect(Collectors.toList());

        factura.setDetalles(detalles);

        // Total y IVA
        BigDecimal total = detalles.stream()
                .map(DetalleFactura::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal iva = total.multiply(BigDecimal.valueOf(0.19)); // 19% IVA

        factura.setTotal(total);
        factura.setIva(iva);

        // Guardar factura
        facturaRepository.save(factura);

        return mapToDTO(factura);
    }




    public FacturaDTO obtenerFacturaPorPedido(Long pedidoId) {
        Optional<Factura> facturaOpt = facturaRepository.findByPedidoId(pedidoId);

        if (facturaOpt.isPresent()) {
            return mapToDTO(facturaOpt.get()); // ← ✅ CORREGIDO
        }

        return generarFactura(pedidoId);
    }

    private FacturaDTO mapToDTO(Factura factura) {
        List<DetalleFacturaDTO> detallesDTO = factura.getDetalles().stream().map(detalle ->
                new DetalleFacturaDTO(
                        detalle.getProductoNombre(),
                        detalle.getCantidad(),
                        detalle.getPrecioUnitario(),
                        detalle.getSubtotal()
                )).collect(Collectors.toList());

        return new FacturaDTO(
                factura.getId(),
                factura.getNumero(),
                factura.getFechaEmision(),
                factura.getUsuario().getNombre(),
                factura.getTotal(),
                factura.getIva(),
                factura.getMetodoPago(),
                detallesDTO
        );
    }
}
