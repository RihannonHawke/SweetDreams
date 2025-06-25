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
import org.springframework.web.bind.annotation.RequestBody; // Usamos el @RequestBody de Spring directamente
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.SweetDreams.logistica.modelo.Producto;
import com.example.SweetDreams.logistica.repositorio.ProductoRepositorio;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/productos")
@Tag(name = "Productos", description = "Operaciones relacionadas con la gestión de productos en el sistema de logística.")
public class ProductoControlador {

    private final ProductoRepositorio productoRepositorio;

    @Autowired
    public ProductoControlador(ProductoRepositorio productoRepositorio) {
        this.productoRepositorio = productoRepositorio;
    }

    @Operation(summary = "Obtener todos los productos", description = "Retorna una lista de todos los productos disponibles en el inventario.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de productos obtenida exitosamente.",
                     content = @Content(mediaType = "application/json", // La guía usa "application/json" directamente
                                        schema = @Schema(implementation = Producto.class)))
    })
    @GetMapping
    public ResponseEntity<List<Producto>> getAllProducts() {
        List<Producto> productos = productoRepositorio.findAll();
        return new ResponseEntity<>(productos, HttpStatus.OK);
    }

    @Operation(summary = "Obtener un producto por ID", description = "Busca y retorna un producto específico usando su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Producto encontrado exitosamente.",
                     content = @Content(mediaType = "application/json",
                                        schema = @Schema(implementation = Producto.class))),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado.")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Producto> getProductById(
            @Parameter(description = "ID del producto a buscar.", required = true) @PathVariable Long id) {
        Optional<Producto> producto = productoRepositorio.findById(id);
        return producto.map(response -> ResponseEntity.ok().body(response))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(summary = "Crear un nuevo producto", description = "Permite añadir un nuevo producto al inventario.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Producto creado exitosamente.",
                     content = @Content(mediaType = "application/json",
                                        schema = @Schema(implementation = Producto.class))),
        @ApiResponse(responseCode = "400", description = "Solicitud de creación de producto inválida.")
    })
    @PostMapping
    public ResponseEntity<Producto> crearProducto(
            // Confiamos en que @RequestBody de Spring sea suficiente, la guía lo usa así
            @RequestBody Producto producto) {
        Producto nuevoProducto = productoRepositorio.save(producto);
        return new ResponseEntity<>(nuevoProducto, HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar un producto existente", description = "Actualiza los datos de un producto existente usando su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Producto actualizado exitosamente.",
                     content = @Content(mediaType = "application/json",
                                        schema = @Schema(implementation = Producto.class))),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado."),
        @ApiResponse(responseCode = "400", description = "Solicitud de actualización de producto inválida.")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizarProducto(
            @Parameter(description = "ID del producto a actualizar.", required = true) @PathVariable Long id,
            @RequestBody Producto producto) {
        Optional<Producto> productoExistente = productoRepositorio.findById(id);
        if (productoExistente.isPresent()) {
            producto.setId(id); // Asegurar que el ID sea el correcto para la actualización
            Producto productoActualizado = productoRepositorio.save(producto);
            return new ResponseEntity<>(productoActualizado, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Eliminar un producto", description = "Elimina un producto del inventario usando su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Producto eliminado exitosamente (No Content)."),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado.")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> borrarProducto(
            @Parameter(description = "ID del producto a eliminar.", required = true) @PathVariable Long id) {
        Optional<Producto> productoABorrar = productoRepositorio.findById(id);
        if (productoABorrar.isPresent()) {
            productoRepositorio.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}