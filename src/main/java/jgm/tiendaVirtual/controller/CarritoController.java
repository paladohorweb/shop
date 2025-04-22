package jgm.tiendaVirtual.controller;

import jgm.tiendaVirtual.model.*;
import jgm.tiendaVirtual.repository.CarritoRepository;
import jgm.tiendaVirtual.repository.CarritoItemRepository;
import jgm.tiendaVirtual.repository.ProductoRepository;
import jgm.tiendaVirtual.repository.UsuarioRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import jgm.tiendaVirtual.dto.AgregarProductoRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

@RestController
@RequestMapping("/api/carrito")
@CrossOrigin(origins = "http://localhost:4200") // 🔹 Permitir peticiones desde Angular
public class CarritoController {

    private final CarritoRepository carritoRepository;
    private final CarritoItemRepository carritoItemRepository;
    private final ProductoRepository productoRepository;
    private final UsuarioRepository usuarioRepository;

    public CarritoController(CarritoRepository carritoRepository,
            CarritoItemRepository carritoItemRepository,
            ProductoRepository productoRepository,
            UsuarioRepository usuarioRepository) {
        this.carritoRepository = carritoRepository;
        this.carritoItemRepository = carritoItemRepository;
        this.productoRepository = productoRepository;
        this.usuarioRepository = usuarioRepository;
    }

  
    // 🔹 Eliminar producto del carrito
    @DeleteMapping("/eliminar/{usuarioId}/{productoId}")
    public ResponseEntity<Carrito> eliminarProducto(@PathVariable Long usuarioId, @PathVariable Long productoId) {
        Optional<Carrito> carritoOpt = carritoRepository.findByUsuarioId(usuarioId);

        if (carritoOpt.isPresent()) {
            Carrito carrito = carritoOpt.get();

            carrito.getItems().removeIf(item -> item.getProducto().getId().equals(productoId));

            carrito.calcularTotal();
            carritoRepository.save(carrito);

            return ResponseEntity.ok(carrito);
        }

        return ResponseEntity.notFound().build();
    }

    // 🔹 Vaciar carrito
    @DeleteMapping("/vaciar/{usuarioId}")
    public ResponseEntity<Void> vaciarCarrito(@PathVariable Long usuarioId) {
        Optional<Carrito> carritoOpt = carritoRepository.findByUsuarioId(usuarioId);
        carritoOpt.ifPresent(carritoRepository::delete);
        return ResponseEntity.ok().build();
    }
    
    
    
    
        // 🔹 Obtener carrito del usuario autenticado
    @GetMapping("/{usuarioId}")
 // ✅ Requiere autenticación
    public ResponseEntity<Carrito> obtenerCarrito(@PathVariable Long usuarioId) {
        Optional<Carrito> carritoOpt = carritoRepository.findByUsuarioId(usuarioId);
        return carritoOpt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    
    // 🔹 Agregar producto al carrito (ahora requiere autenticación)
    @PostMapping("/agregar") // ✅ Verifica que el usuario esté autenticado
    public ResponseEntity<Carrito> agregarProducto(@RequestBody AgregarProductoRequest request) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(request.getUsuarioId());
        Optional<Producto> productoOpt = productoRepository.findById(request.getProductoId());

        if (usuarioOpt.isPresent() && productoOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            Producto producto = productoOpt.get();

            Carrito carrito = carritoRepository.findByUsuarioId(usuario.getId())
                    .orElseGet(() -> {
                        Carrito nuevoCarrito = new Carrito(null, usuario, new ArrayList(), BigDecimal.ZERO);
                        return carritoRepository.save(nuevoCarrito);
                    });

            Optional<CarritoItem> itemExistente = carrito.getItems().stream()
                    .filter(item -> item.getProducto().getId().equals(request.getProductoId()))
                    .findFirst();

            if (itemExistente.isPresent()) {
                itemExistente.get().setCantidad(itemExistente.get().getCantidad() + request.getCantidad());
            } else {
                CarritoItem nuevoItem = new CarritoItem(null, carrito, producto, request.getCantidad(), producto.getPrecio());
                carrito.getItems().add(nuevoItem);
            }

            carrito.calcularTotal();
            carritoRepository.save(carrito);

            return ResponseEntity.ok(carrito);
        }

        return ResponseEntity.badRequest().build();
    }
}
    
    
    
    
    
    
    
      // 🔹 Obtener carrito del usuario autenticado
//    @GetMapping
//    public ResponseEntity<Carrito> obtenerCarrito(@AuthenticationPrincipal Usuario usuario) {
//        if (usuario == null) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//        }
//
//        Optional<Carrito> carritoOpt = carritoRepository.findByUsuarioId(usuario.getId());
//        return carritoOpt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
//    }

    // 🔹 Agregar producto al carrito
//    @PostMapping("/agregar")
//    public ResponseEntity<Carrito> agregarProducto(
//            @AuthenticationPrincipal Usuario usuario,
//            @RequestParam Long productoId,
//            @RequestParam int cantidad) {
//
//        if (usuario == null) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//        }
//
//        Optional<Producto> productoOpt = productoRepository.findById(productoId);
//
//        if (productoOpt.isPresent()) {
//            Producto producto = productoOpt.get();
//
//            // Obtener o crear el carrito del usuario
//            Carrito carrito = carritoRepository.findByUsuarioId(usuario.getId())
//                    .orElseGet(() -> {
//                        Carrito nuevoCarrito = new Carrito(null, usuario, new HashSet<>(), BigDecimal.ZERO);
//                        return carritoRepository.save(nuevoCarrito);
//                    });
//
//            // Buscar si el producto ya está en el carrito
//            Optional<CarritoItem> itemExistente = carrito.getItems().stream()
//                    .filter(item -> item.getProducto().getId().equals(productoId))
//                    .findFirst();
//
//            if (itemExistente.isPresent()) {
//                itemExistente.get().setCantidad(itemExistente.get().getCantidad() + cantidad);
//            } else {
//                CarritoItem nuevoItem = new CarritoItem(null, carrito, producto, cantidad, producto.getPrecio());
//                carrito.getItems().add(nuevoItem);
//            }
//
//            carrito.calcularTotal();
//            carritoRepository.save(carrito);
//
//            return ResponseEntity.ok(carrito);
//        }
//
//        return ResponseEntity.badRequest().build();
//    }

    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    

