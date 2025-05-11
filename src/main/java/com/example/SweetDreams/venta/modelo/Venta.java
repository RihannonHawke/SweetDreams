package com.example.SweetDreams.venta.modelo;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Venta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idVenta;
    private String producto;
    private int cantidad;
    private double precioUnitario;
    private String metodoPago;
    private LocalDate fechaVenta;
    private Long clienteId;
}
