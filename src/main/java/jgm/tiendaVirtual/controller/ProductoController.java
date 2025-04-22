package jgm.tiendaVirtual.controller;

import jgm.tiendaVirtual.model.Producto;
import jgm.tiendaVirtual.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.transaction.annotation.Transactional;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    /** 🔹 Agregar un producto
     * @param producto
     * @return  */
    @PostMapping
        @Transactional
    public ResponseEntity<?> agregarProducto(@RequestBody Producto producto) {
        try {
            Producto nuevoProducto = productoService.agregarProducto(producto);
            return ResponseEntity.ok(nuevoProducto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al agregar producto: " + e.getMessage());
        }
    }

    /** 🔹 Listar todos los productos
     * @return  */
    @GetMapping
        @Transactional
    public ResponseEntity<List<Producto>> listarProductos() {
        return ResponseEntity.ok(productoService.listarProductos());
    }

    /** 🔹 Obtener producto por ID
     * @param id
     * @return  */
    @GetMapping("/{id}")
        @Transactional
    public ResponseEntity<?> obtenerProductoPorId(@PathVariable Long id) {
        try {
            Producto producto = productoService.obtenerProductoPorId(id);
            return ResponseEntity.ok(producto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    /** 🔹 Actualizar un producto
     * @param id
     * @param producto
     * @return  */
    @PutMapping("/{id}")
        @Transactional
    public ResponseEntity<?> actualizarProducto(@PathVariable Long id, @RequestBody Producto producto) {
        try {
            Producto productoActualizado = productoService.actualizarProducto(id, producto);
            return ResponseEntity.ok(productoActualizado);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al actualizar producto: " + e.getMessage());
        }
    }

    /** 🔹 Eliminar un producto
     * @param id
     * @return  */
    @DeleteMapping("/{id}")
        @Transactional
    public ResponseEntity<?> eliminarProducto(@PathVariable Long id) {
        try {
            productoService.eliminarProducto(id);
            return ResponseEntity.ok("Producto eliminado correctamente");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al eliminar producto: " + e.getMessage());
        }
    }
}
