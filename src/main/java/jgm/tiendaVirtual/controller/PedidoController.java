package jgm.tiendaVirtual.controller;

import java.math.BigDecimal;
import java.util.*;

import jgm.tiendaVirtual.dto.PedidoDTO;
import jgm.tiendaVirtual.model.Pedido;
import jgm.tiendaVirtual.service.PedidoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jgm.tiendaVirtual.dto.DetallePedidoDTO;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private static final Logger logger = LoggerFactory.getLogger(PedidoController.class);

    @Autowired
    private PedidoService pedidoService;

    /**
     * 🔹 Crear un nuevo pedido
     *
     * @param pedidoDTO
     * @return
     */
    @PostMapping("/crearPedido")
    public ResponseEntity<?> crearPedido(@RequestBody PedidoDTO pedidoDTO) {
        try {
            // Validaciones (se mantienen igual)
            if (pedidoDTO == null || pedidoDTO.getUsuarioId() == null
                    || pedidoDTO.getDetalles() == null || pedidoDTO.getDetalles().isEmpty()) {
                return ResponseEntity.badRequest().body("El pedido debe tener un usuario y al menos un detalle.");
            }

            for (DetallePedidoDTO detalle : pedidoDTO.getDetalles()) {
                if (detalle.getCantidad() <= 0) {
                    return ResponseEntity.badRequest().body("La cantidad debe ser mayor que cero.");
                }
                if (detalle.getPrecioUnitario().compareTo(BigDecimal.ZERO) < 0) {
                    return ResponseEntity.badRequest().body("El precio no puede ser negativo.");
                }
            }

            // Creación del pedido
            Pedido nuevoPedido = pedidoService.crearPedido(pedidoDTO.getUsuarioId(), pedidoDTO.getDetalles());

            // 🔥 Respuesta mejorada con estructura JSON
            Map<String, Object> response = new LinkedHashMap<>();
            response.put("status", "success");
            response.put("code", HttpStatus.CREATED.value());
            response.put("message", "Pedido creado exitosamente");
            response.put("data", Map.of(
                    "id", nuevoPedido.getId(),
                    "fecha", nuevoPedido.getFechaPedido(),
                    "total", nuevoPedido.getTotal(),
                    "estado", nuevoPedido.getEstado()
            ));

            return new ResponseEntity<>(response, HttpStatus.CREATED);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error al procesar el pedido: ", e);

            Map<String, Object> errorResponse = new LinkedHashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("code", HttpStatus.INTERNAL_SERVER_ERROR.value());
            errorResponse.put("message", "Error al procesar el pedido");
            errorResponse.put("details", e.getMessage());

            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 🔹 Obtener pedidos por usuario
     *
     * @param idUsuario
     * @return
     */
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<?> obtenerPedidosPorUsuario(@PathVariable Long idUsuario) {
        try {
            logger.info("📌 Buscando pedidos para el usuario ID: {}", idUsuario);
            List<Pedido> pedidos = pedidoService.obtenerPedidosPorUsuario(idUsuario);

            if (pedidos.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok(pedidos);

        } catch (Exception e) {
            logger.error("❌ Error al obtener pedidos del usuario {}: {}", idUsuario, e.getMessage(), e);
            return ResponseEntity.internalServerError().body("Error al recuperar los pedidos.");
        }
    }

    /**
     * 🔹 Listar todos los pedidos
     *
     * @return
     */
    @GetMapping
    public ResponseEntity<?> listarPedidos() {
        try {
            List<Pedido> pedidos = pedidoService.listarPedidos();

            if (pedidos.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok(pedidos);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al obtener la lista de pedidos.");
        }
    }

    /**
     * 🔹 Obtener un pedido específico por ID
     *
     * @param idPedido
     * @return
     */
    @GetMapping("/{idPedido}")
    public ResponseEntity<?> obtenerPedidoPorId(@PathVariable Long idPedido) {
        try {
            Optional<Pedido> pedido = pedidoService.obtenerPedidoPorId(idPedido);
            if (pedido.isPresent()) {
                return ResponseEntity.ok(pedido.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pedido no encontrado.");
            }
        } catch (Exception e) {
            logger.error("❌ Error al obtener el pedido ID {}: {}", idPedido, e.getMessage());
            return ResponseEntity.internalServerError().body("Error al recuperar el pedido.");
        }
    }

    /**
     * 🔹 Actualizar el estado del pedido
     *
     * @param idPedido
     * @param estadoRequest
     * @return
     */
    @PutMapping("/{idPedido}/estado")
    public ResponseEntity<?> actualizarEstadoPedido(
            @PathVariable Long idPedido,
            @RequestBody Map<String, String> estadoRequest
    ) {
        try {
            String nuevoEstado = estadoRequest.get("estado");
            pedidoService.actualizarEstado(idPedido, nuevoEstado);
            return ResponseEntity.ok("Estado actualizado correctamente a: " + nuevoEstado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al actualizar estado: " + e.getMessage());
        }
    }

    /**
     * 🔹 Procesar el pago del pedido
     *
     * @param idPedido
     * @param metodoPago
     * @return
     */
    @PutMapping("/{idPedido}/pagar")
    public ResponseEntity<?> pagarPedido(@PathVariable Long idPedido, @RequestParam String metodoPago) {
        try {
            pedidoService.procesarPago(idPedido, metodoPago);
            return ResponseEntity.ok("Pago realizado con éxito.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al procesar el pago: " + e.getMessage());
        }
    }
    //nueva funcionalidad en process

    @PutMapping("/pagar/{id}")
    public ResponseEntity<Map<String, Object>> pagarPedido(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            pedidoService.pagarPedido(id);
            response.put("message", "Pedido pagado correctamente");
            response.put("status", "success");
            return ResponseEntity.ok()
                    .header("Content-Type", "application/json")
                    .body(response); // Asegura tipo JSON
        } catch (IllegalStateException e) {
            response.put("message", e.getMessage());
            response.put("status", "error");
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            response.put("message", "Error inesperado al pagar el pedido");
            response.put("status", "error");
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @PutMapping("/cancelar/{id}")
    public ResponseEntity<Map<String, Object>> cancelarPedido(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            pedidoService.cancelarPedido(id);
            response.put("message", "Pedido cancelado correctamente");
            response.put("status", "success");
            return ResponseEntity.ok(response);
        } catch (IllegalStateException e) {
            response.put("message", e.getMessage());
            response.put("status", "error");
            return ResponseEntity.badRequest().body(response);
        }
    }
}
