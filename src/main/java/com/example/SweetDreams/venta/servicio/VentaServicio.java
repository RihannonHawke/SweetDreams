package com.example.SweetDreams.venta.servicio;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.SweetDreams.venta.dto.CarritoDTO;
import com.example.SweetDreams.venta.dto.ItemCarritoDTO;
import com.example.SweetDreams.venta.modelo.Venta;
import com.example.SweetDreams.venta.repositorio.VentaRepositorio;


@Service
public class VentaServicio {

    private final VentaRepositorio ventaRepositorio;
    // Si tu VentaServicio necesita un ProductoServicioVenta para obtener precios/productos,
    // descomenta y configúralo.
    // private final ProductoServicioVenta productoServicioVenta;

    @Autowired
    public VentaServicio(VentaRepositorio ventaRepositorio /*, ProductoServicioVenta productoServicioVenta */) {
        this.ventaRepositorio = ventaRepositorio;
        // this.productoServicioVenta = productoServicioVenta;
    }

    // Este es el método que necesita recibir un CarritoDTO
    public Venta registrarVenta(CarritoDTO carritoDTO) {
        Venta nuevaVenta = new Venta();
        nuevaVenta.setClienteId(carritoDTO.getClienteId());
        nuevaVenta.setFechaVenta(LocalDate.now());

        double totalVenta = 0.0;

        if (carritoDTO.getItems() != null) {
            for (ItemCarritoDTO item : carritoDTO.getItems()) {
                // En un escenario real, aquí buscarías el Producto real por su ID
                // para obtener el precio actual. Por simplicidad en el test,
                // estamos usando el precioUnitario que viene en el ItemCarritoDTO.
                totalVenta += item.getPrecioUnitario() * item.getCantidad();
            }
        }
        nuevaVenta.setTotal(totalVenta);
        nuevaVenta.setEstado("COMPLETADO");

        return ventaRepositorio.save(nuevaVenta);
    }

    // Puedes tener otros métodos CRUD aquí, si existen
    // public List<Venta> getAllVentas() { /* ... */ }
    // public Optional<Venta> getVentaById(Long id) { /* ... */ }
    // public void eliminarVenta(Long id) { /* ... */ }
}