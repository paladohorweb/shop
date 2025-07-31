package jgm.tiendaVirtual.service;

import jgm.tiendaVirtual.model.Producto;
import jgm.tiendaVirtual.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    /** 🔹 Agregar un nuevo producto
     * @param producto
     * @return  */
    @Transactional
    public Producto agregarProducto(Producto producto) {
        if (producto.getPrecio() == null || producto.getPrecio().doubleValue() <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor a 0");
        }
        if (producto.getStock() == null || producto.getStock() < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo");
        }
        return productoRepository.save(producto);
    }

    /** 🔹 Listar todos los productos
     * @return  */
        @Transactional
    public List<Producto> listarProductos() {
        return productoRepository.findAll();
    }

    /** 🔹 Obtener producto por ID
     * @param id
     * @return  */
        @Transactional
    public Producto obtenerProductoPorId(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado con ID: " + id));
    }

    /** 🔹 Actualizar un producto existente
     * @param id
     * @param productoActualizado
     * @return  */
        @Transactional
    public Producto actualizarProducto(Long id, Producto productoActualizado) {
        Producto productoExistente = obtenerProductoPorId(id);
        productoExistente.setNombre(productoActualizado.getNombre());
        productoExistente.setDescripcion(productoActualizado.getDescripcion());
        productoExistente.setPrecio(productoActualizado.getPrecio());
        productoExistente.setStock(productoActualizado.getStock());
        productoExistente.setImagenUrl(productoActualizado.getImagenUrl());
        return productoRepository.save(productoExistente);
    }

    /** 🔹 Eliminar un producto
     * @param id */
        @Transactional
    public void eliminarProducto(Long id) {
        if (!productoRepository.existsById(id)) {
            throw new IllegalArgumentException("No se puede eliminar. Producto no encontrado con ID: " + id);
        }
        productoRepository.deleteById(id);
    }

    @Transactional
    public List<Producto> obtenerProductosDestacados() {
        return productoRepository.findByDestacadoTrue();
    }


    @Transactional
    public List<Producto> obtenerProductosMasVendidos() {
        return productoRepository.findTop10ByOrderByCantidadVendidaDesc();
    }
}
