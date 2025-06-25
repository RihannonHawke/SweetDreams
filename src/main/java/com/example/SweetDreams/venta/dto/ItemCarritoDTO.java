package com.example.SweetDreams.venta.dto;

import io.swagger.v3.oas.annotations.media.Schema; // Importa esta línea

@Schema(description = "Representa un producto individual con su cantidad y precio unitario dentro de un carrito de compras.")
public class ItemCarritoDTO {
    @Schema(description = "ID del producto.", example = "101", required = true, type = "integer", format = "int64")
    private Long productoId;

    @Schema(description = "Cantidad del producto a agregar.", example = "2", required = true)
    private int cantidad;

    @Schema(description = "Precio unitario del producto en el momento de la adición al carrito.", example = "2.50", required = false) // Puede ser calculado en el backend
    private double precioUnitario; // Asegúrate de que sea double

    public ItemCarritoDTO() {
    }

    // Constructor de 2 argumentos
    public ItemCarritoDTO(Long productoId, int cantidad) {
        this.productoId = productoId;
        this.cantidad = cantidad;
        this.precioUnitario = 0.0; // Inicializar, el test o el backend lo puede setear después
    }

    // Getters y setters
    public Long getProductoId() { return productoId; }
    public void setProductoId(Long productoId) { this.productoId = productoId; }
    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
    public double getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(double precioUnitario) { this.precioUnitario = precioUnitario; }
}