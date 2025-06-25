package com.example.SweetDreams.venta.modelo;

import io.swagger.v3.oas.annotations.media.Schema; 
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table; 
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "carritos") 
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Representa un ítem en el carrito de compras de un cliente.")
public class Carrito {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único del ítem en el carrito.", example = "1")
    private Long idCarrito;

    @Schema(description = "ID del producto agregado al carrito.", example = "101", type = "integer", format = "int64")
    private Long productoId;

    @Schema(description = "Nombre del producto.", example = "Chocolate Blanco 50g")
    private String producto; // Nombre del producto para facilitar la visualización

    @Schema(description = "Cantidad de este producto en el carrito.", example = "2")
    private int cantidad;

    @Schema(description = "Precio unitario del producto al momento de agregarlo al carrito.", example = "2.50")
    private double precioUnitario;

    @Schema(description = "ID del cliente al que pertenece este carrito.", example = "5", type = "integer", format = "int64")
    private Long clienteId;
}