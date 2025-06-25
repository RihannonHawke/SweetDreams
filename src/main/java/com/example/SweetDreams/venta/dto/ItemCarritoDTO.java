package com.example.SweetDreams.venta.dto;

public class ItemCarritoDTO {
    private Long productoId;
    private int cantidad;
    private double precioUnitario; // Asegúrate de que sea double

    public ItemCarritoDTO() {
    }

    // Constructor de 2 argumentos
    public ItemCarritoDTO(Long productoId, int cantidad) {
        this.productoId = productoId;
        this.cantidad = cantidad;
        this.precioUnitario = 0.0; // Inicializar, el test lo puede setear después
    }

    // Getters y setters
    public Long getProductoId() { return productoId; }
    public void setProductoId(Long productoId) { this.productoId = productoId; }
    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
    public double getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(double precioUnitario) { this.precioUnitario = precioUnitario; }
}