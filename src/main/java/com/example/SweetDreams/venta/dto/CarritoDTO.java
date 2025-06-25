package com.example.SweetDreams.venta.dto;

import java.util.List; // Importa esta línea

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Objeto de transferencia de datos para agregar productos al carrito de un cliente. Contiene el ID del cliente y una lista de ítems a añadir.")
public class CarritoDTO {
    @Schema(description = "ID del cliente al que se agregará o modificará el carrito.", example = "5", required = true)
    private Long clienteId;

    @Schema(description = "Lista de ítems (productos y cantidades) a añadir al carrito.", required = true)
    private List<ItemCarritoDTO> items;

    public CarritoDTO() {
    }

    // Constructor de 2 argumentos
    public CarritoDTO(Long clienteId, List<ItemCarritoDTO> items) {
        this.clienteId = clienteId;
        this.items = items;
    }

    // Getters y Setters
    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public List<ItemCarritoDTO> getItems() {
        return items;
    }

    public void setItems(List<ItemCarritoDTO> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "CarritoDTO{" +
                "clienteId=" + clienteId +
                ", items=" + items +
                '}';
    }
}