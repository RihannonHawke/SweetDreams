package com.example.SweetDreams.Usuario.Repository;

import com.example.SweetDreams.Usuario.Model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    // Aquí puedes agregar métodos personalizados si es necesario

}
