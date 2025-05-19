package com.example.SweetDreams.venta.dto;

import java.util.List;

public class CarritoDTO {
    private Long clienteId;
    private List<ItemCarritoDTO> items;

    // Getters y setters
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
}