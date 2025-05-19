package com.example.SweetDreams.venta.servicio;

import com.example.SweetDreams.venta.dto.ProductoDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class ProductoServicioVenta {

    private final WebClient webClient;

    // Inyección de WebClient configurado en WebClientConfig
    public ProductoServicioVenta(WebClient webClient) {
        this.webClient = webClient;
    }

    // Método para obtener un producto por id desde el microservicio logística
    public ProductoDTO obtenerProductoPorId(Long id) {
        return webClient.get()
                .uri("/productos/{id}", id)
                .retrieve()
                .bodyToMono(ProductoDTO.class)
                .block();  // Llamada síncrona para esperar la respuesta
    }
}