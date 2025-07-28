package jgm.tiendaVirtual.service;


import jgm.tiendaVirtual.dto.SolicitudCreditoDTO;
import jgm.tiendaVirtual.model.*;
import jgm.tiendaVirtual.repository.CreditoRepository;
import jgm.tiendaVirtual.repository.PedidoRepository;
import jgm.tiendaVirtual.repository.ProductoRepository;
import jgm.tiendaVirtual.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CreditoService {
    private final CreditoRepository creditoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProductoRepository productoRepository;
    private final PedidoRepository   pedidoRepository;

    public Credito solicitarCredito(Long userId, SolicitudCreditoDTO dto) {
        Usuario usuario = usuarioRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Producto producto = productoRepository.findById(dto.getProductoId())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        Pedido pedido = pedidoRepository.findById(dto.getPedidoId())
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        BigDecimal interes = new BigDecimal("0.12");
        BigDecimal montoFinal = dto.getMontoSolicitado().multiply(BigDecimal.ONE.add(interes));

        Credito credito = Credito.builder()
                .usuario(usuario)
                .producto(producto)
                .pedido(pedido)
                .montoSolicitado(dto.getMontoSolicitado())
                .cuotas(dto.getCuotas())
                .interes(interes)
                .montoFinal(montoFinal)
                .estado(Credito.EstadoCredito.PENDIENTE)
                .fechaSolicitud(LocalDateTime.now())
                .build();

        return creditoRepository.save(credito);
    }

    public List<Credito> listarPorUsuario(Long userId) {
        return creditoRepository.findByUsuarioId(userId);
    }

    public List<Credito> listarTodos() {
        return creditoRepository.findAll();
    }

    @Transactional
    public Credito aprobarCredito(Long id) {
        Credito credito = creditoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Crédito no encontrado"));

        Pedido pedido = credito.getPedido();
        if (pedido == null) {
            throw new RuntimeException("Este crédito no tiene un pedido asociado");
        }

        // Descontar stock de cada producto en el pedido
        for (DetallePedido item : pedido.getDetalles()) {
            Producto producto = item.getProducto();
            int cantidad = item.getCantidad();

            if (producto.getStock() < cantidad) {
                throw new RuntimeException("Stock insuficiente para el producto: " + producto.getNombre());
            }

            producto.setStock(producto.getStock() - cantidad);
            productoRepository.save(producto); // Asegurarse de guardar el producto actualizado
        }

        // Marcar pedido como pagado
        pedido.setEstado(EstadoPedido.PAGADO);
        pedidoRepository.save(pedido);

        // Marcar crédito como aprobado
        credito.setEstado(Credito.EstadoCredito.APROBADO);
        credito.setFechaAprobacion(LocalDateTime.now());

        return creditoRepository.save(credito);
    }

    @Transactional
    public Credito rechazarCredito(Long id) {
        Credito credito = creditoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Crédito no encontrado"));

        credito.setEstado(Credito.EstadoCredito.RECHAZADO);
        credito.setFechaAprobacion(null); // por si acaso

        return creditoRepository.save(credito);
    }


}
