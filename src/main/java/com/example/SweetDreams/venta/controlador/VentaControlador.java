package com.example.SweetDreams.venta.controlador;

import com.example.SweetDreams.venta.modelo.Carrito;
import com.example.SweetDreams.venta.modelo.Venta;
import com.example.SweetDreams.venta.repositorio.CarritoRespositorio;
import com.example.SweetDreams.venta.repositorio.VentaRepositorio;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/ventas")
public class VentaControlador {

    private final VentaRepositorio ventaRepositorio;
    private final CarritoRespositorio carritoRepositorio;

    public VentaControlador(VentaRepositorio ventaRepositorio, CarritoRespositorio carritoRepositorio) {
        this.ventaRepositorio = ventaRepositorio;
        this.carritoRepositorio = carritoRepositorio;
    }

    // Registrar una venta directa
    @PostMapping
    public ResponseEntity<Venta> registrar(@RequestBody Venta venta) {
        venta.setFechaVenta(LocalDate.now());
        Venta ventaGuardada = ventaRepositorio.save(venta);
        return new ResponseEntity<>(ventaGuardada, HttpStatus.CREATED);
    }

    // Obtener todas las ventas
    @GetMapping
    public ResponseEntity<List<Venta>> obtenerTodas() {
        List<Venta> ventas = ventaRepositorio.findAll();
        return new ResponseEntity<>(ventas, HttpStatus.OK);
    }

    // Obtener una venta por ID
    @GetMapping("/{id}")
    public ResponseEntity<Venta> obtenerPorId(@PathVariable Long id) {
        return ventaRepositorio.findById(id)
                .map(venta -> new ResponseEntity<>(venta, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Eliminar una venta por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarVentaPorId(@PathVariable Long id) {
        if (ventaRepositorio.existsById(id)) {
            ventaRepositorio.deleteById(id);
            return new ResponseEntity<>("Venta con ID " + id + " eliminada.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Venta con ID " + id + " no encontrada.", HttpStatus.NOT_FOUND);
        }
    }

    // Eliminar todas las ventas de un cliente
    @Transactional
    @DeleteMapping("/cliente/{clienteId}")
    public ResponseEntity<String> eliminarVentasPorCliente(@PathVariable Long clienteId) {
        ventaRepositorio.deleteByClienteId(clienteId);
        return new ResponseEntity<>("Ventas del cliente " + clienteId + " eliminadas.", HttpStatus.OK);
    }

    // Confirmar carrito: transformar productos del carrito en ventas
    @PostMapping("/confirmar-carrito")
    public ResponseEntity<String> confirmarCarrito(@RequestParam Long clienteId, @RequestParam String metodoPago) {
        List<Carrito> carritoItems = carritoRepositorio.findByClienteId(clienteId);

        if (carritoItems.isEmpty()) {
            return new ResponseEntity<>("El carrito del cliente " + clienteId + " está vacío.", HttpStatus.BAD_REQUEST);
        }

        for (Carrito item : carritoItems) {
            Venta venta = new Venta();
            venta.setProducto(item.getProducto());
            venta.setCantidad(item.getCantidad());
            venta.setPrecioUnitario(item.getPrecioUnitario());
            venta.setMetodoPago(metodoPago);
            venta.setFechaVenta(LocalDate.now());
            venta.setClienteId(clienteId);

            ventaRepositorio.save(venta);
        }

        carritoRepositorio.deleteByClienteId(clienteId);

        return new ResponseEntity<>("Venta(s) generada(s) y carrito limpiado para cliente " + clienteId, HttpStatus.OK);
    }
}