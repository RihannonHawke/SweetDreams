package com.example.SweetDreams.venta.modelo;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
// import jakarta.persistence.OneToMany; // Si fueras a tener detalles de venta
// import java.util.List; // Si fueras a tener detalles de venta
// import com.example.SweetDreams.venta.modelo.DetalleVenta; // Si fueras a tener detalles de venta

@Entity
public class Venta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idVenta;
    private Long clienteId;
    private LocalDate fechaVenta;
    private double total;   // Usando double como solicitaste
    private String estado;

    // Si tuvieras una relación OneToMany con DetalleVenta, iría aquí:
    // @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, orphanRemoval = true)
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