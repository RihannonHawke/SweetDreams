package com.example.SweetDreams.venta.controlador;

import org.springframework.transaction.annotation.Transactional;
import com.example.SweetDreams.venta.modelo.Carrito;
import com.example.SweetDreams.venta.modelo.Venta;
import com.example.SweetDreams.venta.repositorio.CarritoRespositorio;
import com.example.SweetDreams.venta.repositorio.VentaRepositorio;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/carrito")
public class CarritoControlador {

    private final CarritoRespositorio carritoRepositorio;
    private final VentaRepositorio ventaRepositorio;

    public CarritoControlador(CarritoRespositorio carritoRepositorio, VentaRepositorio ventaRepositorio) {
        this.carritoRepositorio = carritoRepositorio;
        this.ventaRepositorio = ventaRepositorio;
    }

    // Agregar productos al carrito
    @PostMapping
    public Carrito agregar(@RequestBody Carrito item) {
        return carritoRepositorio.save(item);
    }

    // Obtener carrito por cliente
    @GetMapping("/{clienteId}")
    public List<Carrito> obtenerPorCliente(@PathVariable Long clienteId) {
        return carritoRepositorio.findByClienteId(clienteId);
    }

    // Confirmar compra y mover a ventas
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
            venta.setMetodoPago("Efectivo"); // Puedes reemplazarlo por lógica personalizada
            venta.setClienteId(item.getClienteId());
            venta.setFechaVenta(java.time.LocalDate.now());

            ventaRepositorio.save(venta);
        }

        carritoRepositorio.deleteByClienteId(clienteId);

        return "Compra confirmada. Productos movidos a ventas.";
    }

    @DeleteMapping("/ventas/{clienteId}")
    public String eliminarVentasPorCliente(@PathVariable Long clienteId) {
        ventaRepositorio.deleteByClienteId(clienteId);
            return "Ventas del cliente " + clienteId + " eliminadas.";
    }
}