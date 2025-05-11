package com.example.SweetDreams.venta.modelo;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Carrito {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCarrito;
    private String producto;
    private int cantidad;
    private double precioUnitario;
    private Long clienteId;
}