package com.example.SweetDreams.logistica.servicio;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.SweetDreams.logistica.modelo.Producto;
import com.example.SweetDreams.logistica.repositorio.ProductoRepositorio;


@Service

public class ProductoServicio {
    private final ProductoRepositorio productoRepositorio;
    
    @Autowired
    public ProductoServicio(ProductoRepositorio productoRepositorio) {
        this.productoRepositorio = productoRepositorio;
    }
    
    // CREAR
    public Producto crearProducto(Producto producto) {
        return productoRepositorio.save(producto);
    }
    
    // BUSCAR
    public List<Producto> getAllProducts() {
        return productoRepositorio.findAll();
    }
    
    // BUSCAR POR ID
    public Optional<Producto> getProductById(Long id) {
        return productoRepositorio.findById(id);
    }
    
    // ACTUALIZAR
    public Producto actualizarProducto(Producto producto) {
        return productoRepositorio.save(producto);
    }
    
    // DELETE
    public void borrarProducto(Long id) {
        productoRepositorio.deleteById(id);
    }

}
