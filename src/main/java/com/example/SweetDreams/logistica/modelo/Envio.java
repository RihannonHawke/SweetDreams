package com.example.SweetDreams.logistica.modelo;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Envio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEnvio;
    private String direccionDestino;
    private String ciudadDestino;
    private Double costoEnvio;
    private String fechaEnvio;
    private double telefono;
    private String estado;

}
