package jgm.tiendaVirtual.controller;


import jgm.tiendaVirtual.dto.SolicitudCreditoDTO;
import jgm.tiendaVirtual.model.Credito;
import jgm.tiendaVirtual.service.CreditoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/creditos")
@RequiredArgsConstructor
public class CreditoController {
    private final CreditoService creditoService;

    @PostMapping("/solicitar/{userId}")
    public ResponseEntity<Credito> solicitar(@PathVariable Long userId, @RequestBody SolicitudCreditoDTO dto) {
        return ResponseEntity.ok(creditoService.solicitarCredito(userId, dto));
    }

    @GetMapping("/usuario/{userId}")
    public ResponseEntity<List<Credito>> listarUsuario(@PathVariable Long userId) {
        return ResponseEntity.ok(creditoService.listarPorUsuario(userId));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public ResponseEntity<List<Credito>> listarTodos() {
        return ResponseEntity.ok(creditoService.listarTodos());
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/aprobar/{id}")
    public ResponseEntity<Credito> aprobar(@PathVariable Long id) {
        return ResponseEntity.ok(creditoService.aprobarCredito(id));
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/rechazar/{id}")
    public ResponseEntity<Credito> rechazar(@PathVariable Long id) {
        return ResponseEntity.ok(creditoService.rechazarCredito(id));
    }
}
