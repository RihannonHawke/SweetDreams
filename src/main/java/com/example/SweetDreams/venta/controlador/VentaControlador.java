package com.example.SweetDreams.venta.controlador;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.SweetDreams.venta.modelo.Venta;
import com.example.SweetDreams.venta.repositorio.VentaRepositorio;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/ventas")
@Tag(name = "Ventas", description = "Operaciones relacionadas con el registro y gestión de ventas.")
public class VentaControlador {

    private final VentaRepositorio ventaRepositorio;

    public VentaControlador(VentaRepositorio ventaRepositorio) {
        this.ventaRepositorio = ventaRepositorio;
    }

    @Operation(summary = "Registrar una nueva venta", description = "Registra una venta directa en el sistema. La fecha de venta se establece automáticamente al día actual.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Venta registrada exitosamente.",
                     content = @Content(mediaType = "application/json",
                                        schema = @Schema(implementation = Venta.class))),
        @ApiResponse(responseCode = "400", description = "Solicitud de venta inválida.")
    })
    @PostMapping
    public ResponseEntity<Venta> registrar(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Objeto Venta a registrar. La fecha de venta se establecerá automáticamente.", required = true)
            @RequestBody Venta venta) {
        venta.setFechaVenta(LocalDate.now());
        Venta ventaGuardada = ventaRepositorio.save(venta);
        return new ResponseEntity<>(ventaGuardada, HttpStatus.CREATED);
    }

    @Operation(summary = "Obtener todas las ventas", description = "Retorna una lista de todas las ventas registradas en el sistema.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de ventas obtenida exitosamente.",
                     content = @Content(mediaType = "application/json",
                                        schema = @Schema(implementation = Venta.class)))
    })
    @GetMapping
    public ResponseEntity<List<Venta>> obtenerTodas() {
        List<Venta> ventas = ventaRepositorio.findAll();
        return new ResponseEntity<>(ventas, HttpStatus.OK);
    }

    @Operation(summary = "Obtener una venta por ID", description = "Busca y retorna una venta específica usando su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Venta encontrada exitosamente.",
                     content = @Content(mediaType = "application/json",
                                        schema = @Schema(implementation = Venta.class))),
        @ApiResponse(responseCode = "404", description = "Venta no encontrada.")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Venta> obtenerPorId(
            @Parameter(description = "ID de la venta a buscar.", required = true) @PathVariable Long id) {
        return ventaRepositorio.findById(id)
                .map(venta -> new ResponseEntity<>(venta, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(summary = "Eliminar una venta por ID", description = "Elimina una venta del sistema usando su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Venta eliminada exitosamente."),
        @ApiResponse(responseCode = "404", description = "Venta no encontrada.")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarVentaPorId(
            @Parameter(description = "ID de la venta a eliminar.", required = true) @PathVariable Long id) {
        if (ventaRepositorio.existsById(id)) {
            ventaRepositorio.deleteById(id);
            return new ResponseEntity<>("Venta con ID " + id + " eliminada.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Venta con ID " + id + " no encontrada.", HttpStatus.NOT_FOUND);
        }
    }

    @Transactional
    @Operation(summary = "Eliminar ventas por cliente", description = "Elimina todas las ventas asociadas a un cliente específico. Requiere un método `deleteByClienteId` en el repositorio.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Ventas del cliente eliminadas exitosamente."),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor (ej. si el método deleteByClienteId no existe o falla).")
    })
    @DeleteMapping("/cliente/{clienteId}")
    public ResponseEntity<String> eliminarVentasPorCliente(
            @Parameter(description = "ID del cliente cuyas ventas se eliminarán.", required = true) @PathVariable Long clienteId) {
        ventaRepositorio.deleteByClienteId(clienteId);
        return new ResponseEntity<>("Ventas del cliente " + clienteId + " eliminadas.", HttpStatus.OK);
    }
}