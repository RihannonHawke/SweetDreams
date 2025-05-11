package com.example.SweetDreams.venta.controlador;

import com.example.SweetDreams.venta.modelo.Carrito;
import com.example.SweetDreams.venta.repositorio.CarritoRespositorio;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/carrito")
public class CarritoControlador {

    private final CarritoRespositorio carritoRepositorio;

    public CarritoControlador(CarritoRespositorio carritoRepositorio) {
        this.carritoRepositorio = carritoRepositorio;
    }

    @PostMapping
    public Carrito agregar(@RequestBody Carrito item) {
        return carritoRepositorio.save(item);
    }

    @GetMapping("/{clienteId}")
    public List<Carrito> obtenerPorCliente(@PathVariable Long clienteId) {
        return carritoRepositorio.findByClienteId(clienteId);
    }
}