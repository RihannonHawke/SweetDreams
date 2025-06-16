package com.example.SweetDreams.venta.dto;

import java.util.List;

public class CarritoDTO {
    private Long clienteId;
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