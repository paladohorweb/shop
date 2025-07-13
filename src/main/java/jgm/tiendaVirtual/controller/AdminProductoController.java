package jgm.tiendaVirtual.controller;

import jgm.tiendaVirtual.model.Producto;
//import jgm.tiendaVirtual.repository.PedidoRepository;
import jgm.tiendaVirtual.repository.ProductoRepository;
//import jgm.tiendaVirtual.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/admin/productos")
@PreAuthorize("hasRole('ADMIN')")
@CrossOrigin(origins = "http://localhost:4200")
public class AdminProductoController {
    @Autowired
    private ProductoRepository productoRepo;

    @GetMapping
    public List<Producto> listar() {
        return productoRepo.findAll();
    }

    @PostMapping
    public Producto crear(@RequestBody Producto p) {
        return productoRepo.save(p);
    }

    @PutMapping("/{id}")
    public Producto actualizar(@PathVariable Long id, @RequestBody Producto p) {
        Producto existente = productoRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        existente.setNombre(p.getNombre());
        existente.setDescripcion(p.getDescripcion());
        existente.setPrecio(p.getPrecio());
        existente.setStock(p.getStock());
        existente.setImagenUrl(p.getImagenUrl());
        return productoRepo.save(existente);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        productoRepo.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}