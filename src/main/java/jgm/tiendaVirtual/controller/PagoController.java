package jgm.tiendaVirtual.controller;

import jgm.tiendaVirtual.dto.PagoDTO;
import jgm.tiendaVirtual.model.EstadoPago;
import jgm.tiendaVirtual.model.MetodoPago;
import jgm.tiendaVirtual.service.PagoService;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pagos")
public class PagoController {

    private final PagoService pagoService;

    // ✅ Constructor Injection (mejor que @Autowired)
    public PagoController(PagoService pagoService) {
        this.pagoService = pagoService;
    }

    /**
     * 🔹 Procesar un pago
     *
     * @param pedidoId
     * @param metodoPago
     * @return
     */
    @PostMapping("/procesar/{pedidoId}")
    @Transactional
    public ResponseEntity<?> procesarPago(@PathVariable Long pedidoId, @RequestParam String metodoPago) {
        MetodoPago metodo = MetodoPago.valueOf(metodoPago.toUpperCase()); // Convertimos el String a Enum
        PagoDTO pagoDTO = pagoService.procesarPago(pedidoId, metodo);
        return ResponseEntity.ok(pagoDTO);
    }

    /**
     * 🔹 Actualizar el estado de un pago
     *
     * @param pagoId
     * @param estadoPago
     * @return
     */
    @PutMapping("/actualizar/{pagoId}")
    @Transactional
    public ResponseEntity<?> actualizarEstadoPago(@PathVariable Long pagoId, @RequestParam String estadoPago) {
        try {
            EstadoPago estado = EstadoPago.valueOf(estadoPago.toUpperCase()); // ✅ Conversión segura a Enum
            PagoDTO pagoDTO = pagoService.actualizarEstadoPago(pagoId, estado);
            return ResponseEntity.ok(pagoDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("❌ Error: Estado de pago no válido.");
        }
    }

    /**
     * 🔹 Manejo global de errores
     *
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> manejarExcepciones(Exception e) {
        return ResponseEntity.badRequest().body("❌ Error: " + e.getMessage());
    }
}
