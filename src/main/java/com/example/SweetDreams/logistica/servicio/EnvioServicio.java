package com.example.SweetDreams.logistica.servicio;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.SweetDreams.logistica.modelo.Envio;
import com.example.SweetDreams.logistica.repositorio.EnvioRepositorio;


@Service
public class EnvioServicio {
private final EnvioRepositorio EnvioRepositorio;
private Long idEnvio;

@Autowired
public EnvioServicio(EnvioRepositorio envioRepositorio) {
    this.EnvioRepositorio = envioRepositorio;
}

// Crear envío
public Envio crearEnvio(Envio envio) {
    return EnvioRepositorio.save(envio);
}

// Mostrar todos los envíos
public List<Envio> getAllEnvios() {
    return EnvioRepositorio.findAll();
}

// Buscar envío por id
public Optional<Envio> getEnvioById(Long idEnvio) {
    return EnvioRepositorio.findById(idEnvio);
}

// Actualizar envío
public Envio actualizarEnvio(Envio envio) {
    return EnvioRepositorio.save(envio);
}

// Borrar envío
public void borrarEnvio(Long idEnvio) {
    EnvioRepositorio.deleteById(idEnvio);
}



public Long getIdEnvio() {
        return idEnvio;
    }
}
// para ver algun cambio
// para ver algun cambio
// para ver algun cambio


