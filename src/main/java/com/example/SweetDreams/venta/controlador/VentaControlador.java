package com.example.SweetDreams.venta.controlador;

import org.springframework.transaction.annotation.Transactional;
import com.example.SweetDreams.venta.modelo.Venta;
import com.example.SweetDreams.venta.repositorio.VentaRepositorio;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ventas")
public class VentaControlador {

    private final VentaRepositorio ventaRepositorio;

    public VentaControlador(VentaRepositorio ventaRepositorio) {
        this.ventaRepositorio = ventaRepositorio;
    }

    @PostMapping
    public Venta registrar(@RequestBody Venta venta) {
        venta.setFechaVenta(java.time.LocalDate.now());
        return ventaRepositorio.save(venta);
    }

    @GetMapping
    public List<Venta> obtenerTodas() {
        return ventaRepositorio.findAll();
    }

    @GetMapping("/{id}")
    public Venta obtenerPorId(@PathVariable Long id) {
        return ventaRepositorio.findById(id).orElse(null);
    }

    @Transactional
    @DeleteMapping("/cliente/{clienteId}")
    public String eliminarVentasPorCliente(@PathVariable Long clienteId) {
    ventaRepositorio.deleteByClienteId(clienteId);
        return "Ventas del cliente " + clienteId + " eliminadas.";
    }

    
}