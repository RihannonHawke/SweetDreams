package com.example.SweetDreams.logistica.modelo;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "sweetdreams_envios")

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

    public void setIdEnvio(Long idEnvio) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    

}
