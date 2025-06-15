package com.example.SweetDreams.venta.controlador;

import java.time.LocalDate;
import java.util.List; // Solo VentaRepositorio

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional; // Para deleteByClienteId si VentaRepositorio lo tiene
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.SweetDreams.venta.modelo.Venta;
import com.example.SweetDreams.venta.repositorio.VentaRepositorio;

@RestController
@RequestMapping("/ventas")
public class VentaControlador {

    private final VentaRepositorio ventaRepositorio;
    // private final CarritoRespositorio carritoRepositorio; // Ya no es necesario inyectar aquí

    public VentaControlador(VentaRepositorio ventaRepositorio /*, CarritoRespositorio carritoRepositorio */) {
        this.ventaRepositorio = ventaRepositorio;
        // this.carritoRepositorio = carritoRepositorio;
    }

    // Registrar una venta directa (si el cliente no pasa por carrito)
    @PostMapping
    public ResponseEntity<Venta> registrar(@RequestBody Venta venta) {
        venta.setFechaVenta(LocalDate.now()); // Establecer la fecha de la venta
        // También puedes establecer un estado inicial y calcular el total si es una venta directa.
        // venta.setEstado("PENDIENTE");
        // venta.setTotal(calcularTotalVentaDirecta(venta)); // Necesitarías esta lógica si aplicara

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

    // Eliminar todas las ventas de un cliente (si tu repositorio tiene este método)
    @Transactional
    @DeleteMapping("/cliente/{clienteId}")
    public ResponseEntity<String> eliminarVentasPorCliente(@PathVariable Long clienteId) {
        // Asegúrate de que VentaRepositorio tiene un método deleteByClienteId(Long)
        ventaRepositorio.deleteByClienteId(clienteId);
        return new ResponseEntity<>("Ventas del cliente " + clienteId + " eliminadas.", HttpStatus.OK);
    }

    // *** ELIMINADO: La lógica de confirmar carrito se manejó en CarritoControlador y VentaServicio ***
    // @PostMapping("/confirmar-carrito") // ESTE MÉTODO DEBE SER ELIMINADO O COMENTADO
    // public ResponseEntity<String> confirmarCarrito(@RequestParam Long clienteId, @RequestParam String metodoPago) {
    //     // ... esta lógica ahora está en VentaServicio y es llamada por CarritoControlador
    // }
}