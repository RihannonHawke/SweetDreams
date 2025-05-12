package com.example.SweetDreams.venta.repositorio;


import com.example.SweetDreams.venta.modelo.Carrito;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CarritoRespositorio extends JpaRepository<Carrito, Long> {
    List<Carrito> findByClienteId(Long clienteId);
    void deleteByClienteId(Long clienteId);
}