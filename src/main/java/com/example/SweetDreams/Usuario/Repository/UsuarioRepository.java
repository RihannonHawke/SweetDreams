package com.example.SweetDreams.Usuario.Repository;

import org .springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.SweetDreams.Usuario.Model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
   
}