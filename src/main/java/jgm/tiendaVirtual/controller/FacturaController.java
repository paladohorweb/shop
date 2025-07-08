package jgm.tiendaVirtual.controller;

import jgm.tiendaVirtual.dto.FacturaDTO;
import jgm.tiendaVirtual.service.FacturaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/facturas")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class FacturaController {

    private final FacturaService facturaService;

    @PostMapping("/generar/{pedidoId}")
    public FacturaDTO generarFactura(@PathVariable Long pedidoId) {
        return facturaService.generarFactura(pedidoId);
    }

    @GetMapping("/pedido/{pedidoId}")
    public FacturaDTO obtenerPorPedido(@PathVariable Long pedidoId) {
        return facturaService.obtenerFacturaPorPedido(pedidoId);
    }
}

