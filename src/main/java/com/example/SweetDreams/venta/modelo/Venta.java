package com.example.SweetDreams.venta.modelo;

import java.time.LocalDate; // Importa esta línea

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id; // Es buena práctica usar @Table si el nombre de la tabla difiere o para claridad
import jakarta.persistence.Table;

@Entity
@Table(name = "ventas") // Asumo que el nombre de la tabla es 'ventas', ajústalo si es diferente
@Schema(description = "Representa una transacción de venta completada en el sistema SweetDreams.")
public class Venta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único de la venta.", example = "1")
    private Long idVenta;

    @Schema(description = "ID del cliente que realizó la venta.", example = "5", type = "integer", format = "int64")
    private Long clienteId;

    @Schema(description = "Fecha en que se realizó la venta.", example = "2024-06-25", format = "date")
    private LocalDate fechaVenta;

    @Schema(description = "Monto total de la venta.", example = "75.50", format = "double")
    private double total;

    @Schema(description = "Estado actual de la venta (ej. 'COMPLETADA', 'PENDIENTE', 'CANCELADA').", example = "COMPLETADA")
    private String estado;

    // Si tuvieras una relación OneToMany con DetalleVenta, iría aquí con sus anotaciones @Schema
    // @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, orphanRemoval = true)
    // @Schema(description = "Lista de detalles de productos incluidos en esta venta.")
    // private List<DetalleVenta> detalles;


    public Venta() {
    }

    // Constructor opcional para conveniencia, pero no es estrictamente necesario para JPA
    public Venta(Long clienteId, LocalDate fechaVenta, double total, String estado) {
        this.clienteId = clienteId;
        this.fechaVenta = fechaVenta;
        this.total = total;
        this.estado = estado;
    }

    // Getters y Setters
    public Long getIdVenta() { return idVenta; }
    public void setIdVenta(Long idVenta) { this.idVenta = idVenta; }

    public Long getClienteId() { return clienteId; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }

    public LocalDate getFechaVenta() { return fechaVenta; }
    public void setFechaVenta(LocalDate fechaVenta) { this.fechaVenta = fechaVenta; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    // Si tuvieras detalles de venta, también necesitarías sus getters/setters:
    // public List<DetalleVenta> getDetalles() { return detalles; }
    // public void setDetalles(List<DetalleVenta> detalles) { this.detalles = detalles; }

    @Override
    public String toString() {
        return "Venta{" +
                "idVenta=" + idVenta +
                ", clienteId=" + clienteId +
                ", fechaVenta=" + fechaVenta +
                ", total=" + total +
                ", estado='" + estado + '\'' +
                '}';
    }
}