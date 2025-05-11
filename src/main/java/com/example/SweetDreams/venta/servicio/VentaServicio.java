package com.example.SweetDreams.venta.servicio;

import com.example.SweetDreams.venta.modelo.Venta;
import com.example.SweetDreams.venta.repositorio.VentaRepositorio;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VentaServicio {
    private final VentaRepositorio ventaRepositorio;

    public VentaServicio(VentaRepositorio ventaRepositorio) {
        this.ventaRepositorio = ventaRepositorio;
    }

    public Venta registrarVenta(Venta venta) {
        venta.setFechaVenta(java.time.LocalDate.now());
        return ventaRepositorio.save(venta);
    }

    public List<Venta> listarVentas() {
        return ventaRepositorio.findAll();
    }
}