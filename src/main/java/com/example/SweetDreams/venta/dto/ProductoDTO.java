package com.example.SweetDreams.venta.dto;

import java.util.Objects;

public class ProductoDTO {
    private Long id;
    private String nombre;
    // private String descripcion; // <-- ¡ASEGÚRATE DE ELIMINAR ESTA LÍNEA SI NO LA USAS!
    private Double precio;    // <-- ¡ASEGÚRATE DE QUE SEA Double!
    private Integer stock;

    public ProductoDTO() {
    }

    // Constructor de 4 argumentos
    public ProductoDTO(Long id, String nombre, Double precio, Integer stock) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    // ASEGÚRATE DE QUE NO HAYA GETTERS/SETTERS PARA 'descripcion'
    public Double getPrecio() { return precio; }
    public void setPrecio(Double precio) { this.precio = precio; }
    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }
}