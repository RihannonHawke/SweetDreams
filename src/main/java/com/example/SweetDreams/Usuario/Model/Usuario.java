package com.example.SweetDreams.Usuario.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor; // Para el constructor con todos los argumentos
import lombok.Data;              // Para getters, setters, toString, equals, hashCode
import lombok.NoArgsConstructor;   // Para el constructor sin argumentos

@Entity
@Data                 // Genera getters, setters, toString, equals y hashCode
@NoArgsConstructor    // Genera un constructor sin argumentos
@AllArgsConstructor   // Genera un constructor con todos los argumentos
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombreUsuario;
    private String email;
    private String password;
}