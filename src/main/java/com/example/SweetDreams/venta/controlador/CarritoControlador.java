package com.example.SweetDreams.venta.controlador;

import com.example.SweetDreams.venta.dto.CarritoDTO;
import com.example.SweetDreams.venta.dto.ItemCarritoDTO;
import com.example.SweetDreams.venta.modelo.Carrito;
import com.example.SweetDreams.venta.modelo.Venta;
import com.example.SweetDreams.venta.repositorio.CarritoRespositorio;
import com.example.SweetDreams.venta.repositorio.VentaRepositorio;
import com.example.SweetDreams.logistica.modelo.Producto;
import com.example.SweetDreams.logistica.repositorio.ProductoRepositorio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/carrito")
public class CarritoControlador {

    private final CarritoRespositorio carritoRepositorio;
    private final VentaRepositorio ventaRepositorio;

    @Autowired
    private ProductoRepositorio productoRepositorio;

    public CarritoControlador(CarritoRespositorio carritoRepositorio, VentaRepositorio ventaRepositorio) {
        this.carritoRepositorio = carritoRepositorio;
        this.ventaRepositorio = ventaRepositorio;
    }

    // Agregar productos al carrito con DTO
    @PostMapping
    public List<Carrito> agregar(@RequestBody CarritoDTO carritoDTO) {
        List<Carrito> carritoItems = new ArrayList<>();

        for (ItemCarritoDTO item : carritoDTO.getItems()) {
            Producto producto = productoRepositorio.findById(item.getProductoId())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + item.getProductoId()));

            Carrito carrito = new Carrito();
            carrito.setClienteId(carritoDTO.getClienteId());
            carrito.setProducto(producto.getNombre());
            carrito.setCantidad(item.getCantidad());
            carrito.setPrecioUnitario(item.getPrecioUnitario());

            carritoItems.add(carritoRepositorio.save(carrito));
        }

        return carritoItems;
    }

    // Obtener productos del carrito por cliente
    @GetMapping("/{clienteId}")
    public List<Carrito> obtenerPorCliente(@PathVariable Long clienteId) {
        return carritoRepositorio.findByClienteId(clienteId);
    }

    // Confirmar compra: mover productos del carrito a ventas y limpiar carrito
    @Transactional
    @PostMapping("/confirmar/{clienteId}")
    public String confirmarCompra(@PathVariable Long clienteId) {
        List<Carrito> carrito = carritoRepositorio.findByClienteId(clienteId);

        if (carrito.isEmpty()) {
            return "El carrito está vacío.";
        }

        for (Carrito item : carrito) {
            Venta venta = new Venta();
            venta.setProducto(item.getProducto());
            venta.setCantidad(item.getCantidad());
            venta.setPrecioUnitario(item.getPrecioUnitario());
            venta.setMetodoPago("Efectivo"); // O reemplaza con lógica real de método de pago
            venta.setClienteId(item.getClienteId());
            venta.setFechaVenta(java.time.LocalDate.now());

            ventaRepositorio.save(venta);
        }

        carritoRepositorio.deleteByClienteId(clienteId);
        return "Compra confirmada. Productos movidos a ventas.";
    }
}