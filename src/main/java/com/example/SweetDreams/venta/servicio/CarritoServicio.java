package com.example.SweetDreams.venta.servicio;

import com.example.SweetDreams.venta.dto.ProductoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CarritoServicio {

    private final ProductoServicioVenta productoServicio;

    // Lista que simula el carrito (en memoria)
    private final List<ProductoDTO> carrito = new ArrayList<>();

    @Autowired
    public CarritoServicio(ProductoServicioVenta productoServicio) {
        this.productoServicio = productoServicio;
    }

    // Agregar un producto al carrito por ID (buscando el producto en microservicio logística)
    public boolean agregarProductoAlCarrito(Long productoId) {
        ProductoDTO producto = productoServicio.obtenerProductoPorId(productoId);
        if (producto != null && producto.getStock() != null && producto.getStock() > 0) {
            carrito.add(producto);
            return true;  // Producto agregado con éxito
        }
        return false; // Producto no existe o no hay stock
    }

    // Obtener lista de productos en el carrito
    public List<ProductoDTO> obtenerProductosDelCarrito() {
        return new ArrayList<>(carrito);
    }

    // Calcular total del carrito
    public double calcularTotal() {
        return carrito.stream()
                .mapToDouble(p -> p.getPrecio() != null ? p.getPrecio() : 0.0)
                .sum();
    }

    // Vaciar el carrito (opcional)
    public void vaciarCarrito() {
        carrito.clear();
    }
}