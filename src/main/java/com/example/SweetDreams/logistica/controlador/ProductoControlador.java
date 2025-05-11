package com.example.SweetDreams.logistica.controlador;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.SweetDreams.logistica.modelo.Producto;
import com.example.SweetDreams.logistica.repositorio.ProductoRepositorio;

@RestController
@RequestMapping("/api/productos")
public class ProductoControlador {

    private final ProductoRepositorio productoRepositorio;

    @Autowired
    public ProductoControlador(ProductoRepositorio productoRepositorio) {
        this.productoRepositorio = productoRepositorio;
    }

    @GetMapping
    public ResponseEntity<List<Producto>> getAllProducts() {
        List<Producto> productos = productoRepositorio.findAll();
        return new ResponseEntity<>(productos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> getProductById(@PathVariable Long id) {
        Optional<Producto> producto = productoRepositorio.findById(id);
        return producto.map(response -> ResponseEntity.ok().body(response))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Producto> crearProducto(@RequestBody Producto producto) {
        Producto nuevoProducto = productoRepositorio.save(producto);
        return new ResponseEntity<>(nuevoProducto, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizarProducto(@PathVariable Long id, @RequestBody Producto producto) {
        Optional<Producto> productoExistente = productoRepositorio.findById(id);
        if (productoExistente.isPresent()) {
            producto.setId(id); // Asegurar que el ID sea el correcto para la actualización
            Producto productoActualizado = productoRepositorio.save(producto);
            return new ResponseEntity<>(productoActualizado, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> borrarProducto(@PathVariable Long id) {
        Optional<Producto> productoABorrar = productoRepositorio.findById(id);
        if (productoABorrar.isPresent()) {
            productoRepositorio.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}