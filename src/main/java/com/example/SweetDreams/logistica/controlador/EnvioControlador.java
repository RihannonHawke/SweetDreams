package com.example.SweetDreams.logistica.controlador;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.SweetDreams.logistica.modelo.Envio;
import com.example.SweetDreams.logistica.repositorio.EnvioRepositorio;

@RestController
@RequestMapping("/api/envios")

public class EnvioControlador {
    private final EnvioRepositorio envioRepositorio;
    @Autowired
    public EnvioControlador(EnvioRepositorio envioRepositorio) {
        this.envioRepositorio = envioRepositorio;
    }

    @GetMapping
    public ResponseEntity<List<Envio>> getAllEnvios() {
        List<Envio> envios = envioRepositorio.findAll();
        return new ResponseEntity<>(envios, HttpStatus.OK);
    }

    @GetMapping("/{idEnvio}")
    public ResponseEntity<Envio> getEnvioById(@PathVariable Long idEnvio) {
        return envioRepositorio.findById(idEnvio)
                .map(response -> ResponseEntity.ok().body(response))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Envio> crearEnvio(@RequestBody Envio envio) {
        Envio nuevoEnvio = envioRepositorio.save(envio);
        return new ResponseEntity<>(nuevoEnvio, HttpStatus.CREATED);
    }

    @PutMapping("/{idEnvio}")
    public ResponseEntity<Envio> actualizarEnvio(@PathVariable Long idEnvio, @RequestBody Envio envio) {
        Optional<Envio> envioExistente = envioRepositorio.findById(idEnvio);
        if (envioExistente.isPresent()) {
            envio.setIdEnvio(idEnvio); 
            Envio envioActualizado = envioRepositorio.save(envio);
            return new ResponseEntity<>(envioActualizado, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{idEnvio}")
    public ResponseEntity<Void> borrarEnvio(@PathVariable Long idEnvio) {
        Optional<Envio> envioABorrar = envioRepositorio.findById(idEnvio);
        if (envioABorrar.isPresent()) {
            envioRepositorio.deleteById(idEnvio);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
