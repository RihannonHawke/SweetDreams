package com.example.SweetDreams.venta.repositorio;

import com.example.SweetDreams.venta.modelo.Venta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VentaRepositorio extends JpaRepository<Venta, Long> {
    List<Venta> findByClienteId(Long clienteId);
    void deleteByClienteId(Long clienteId); // <- este elimina todas las ventas del cliente
}