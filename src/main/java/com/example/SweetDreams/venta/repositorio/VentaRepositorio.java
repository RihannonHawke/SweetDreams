package com.example.SweetDreams.venta.repositorio;

import com.example.SweetDreams.venta.modelo.Venta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VentaRepositorio extends JpaRepository<Venta, Long> {
}