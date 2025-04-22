package jgm.tiendaVirtual.controller;

import jgm.tiendaVirtual.model.MetodoPago;
import jgm.tiendaVirtual.model.Pedido;
import jgm.tiendaVirtual.model.Pago;
import jgm.tiendaVirtual.service.CheckoutService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/checkout")
public class CheckoutController {

    private final CheckoutService checkoutService;

    @Autowired
    public CheckoutController(CheckoutService checkoutService) {
        this.checkoutService = checkoutService;
    }

    @PostMapping
    public ResponseEntity<?> procesarCheckout(
            @RequestParam("pedidoId") Long pedidoId,
            @RequestParam("metodoPago") String metodoPagoStr) {
        
        try {
            // 1. Convertir método de pago a enum
            MetodoPago metodoPago = MetodoPago.valueOf(metodoPagoStr.toUpperCase());
            
            // 2. Procesar checkout
            Pago pago = checkoutService.procesarCheckout(pedidoId, metodoPago);
            
            // 3. Retornar respuesta estándar
            return ResponseEntity.ok().body(
                new RespuestaCheckout(
                    pago.getId(),
                    pago.getEstado().name(),
                    pago.getPedido().getId()
                )
            );
            
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                new ErrorResponse("METODO_PAGO_INVALIDO", "Método de pago no soportado")
            );
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                new ErrorResponse("ERROR_CHECKOUT", e.getMessage())
            );
        }
    }

    // 🔹 Clases internas para respuestas estructuradas
    @lombok.Data
    @AllArgsConstructor
    private static class RespuestaCheckout {
        private Long pagoId;
        private String estado;
        private Long pedidoId;
    }

    @lombok.Data
    @AllArgsConstructor
    private static class ErrorResponse {
        private String codigo;
        private String mensaje;
    }
}

