package com.example.SweetDreams.logistica.repositorio;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.SweetDreams.logistica.modelo.Producto;

@Repository

public interface ProductoRepositorio extends JpaRepository<Producto, Long>{

}
