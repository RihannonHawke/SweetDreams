package com.example.SweetDreams.venta.controlador;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.SweetDreams.logistica.modelo.Producto;
import com.example.SweetDreams.logistica.repositorio.ProductoRepositorio;
import com.example.SweetDreams.venta.dto.CarritoDTO;
import com.example.SweetDreams.venta.dto.ItemCarritoDTO;
import com.example.SweetDreams.venta.modelo.Carrito;
import com.example.SweetDreams.venta.repositorio.CarritoRespositorio;
import com.example.SweetDreams.venta.servicio.VentaServicio;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/carrito")
@Tag(name = "Carrito de Compras", description = "Operaciones para gestionar el carrito de compras de los clientes.")
public class CarritoControlador {

    private final CarritoRespositorio carritoRepositorio;
    private final ProductoRepositorio productoRepositorio;
    private final VentaServicio ventaServicio;

    public CarritoControlador(CarritoRespositorio carritoRepositorio,
                              ProductoRepositorio productoRepositorio,
                              VentaServicio ventaServicio) {
        this.carritoRepositorio = carritoRepositorio;
        this.productoRepositorio = productoRepositorio;
        this.ventaServicio = ventaServicio;
    }

    @Operation(summary = "Agregar productos al carrito", description = "Añade uno o más productos al carrito de compras de un cliente. Si un producto no existe o la solicitud es inválida, devuelve un error.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Productos agregados al carrito exitosamente.",
                     content = @Content(mediaType = "application/json",
                                        schema = @Schema(implementation = Carrito.class))), // Retorna una lista de Carrito
        @ApiResponse(responseCode = "400", description = "Solicitud inválida o carrito vacío."),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado.")
    })
    @PostMapping
    public ResponseEntity<List<Carrito>> agregar(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "DTO que contiene el ID del cliente y una lista de ítems a agregar al carrito.", required = true)
            @RequestBody CarritoDTO carritoDTO) {
        List<Carrito> carritoItemsGuardados = new ArrayList<>();

        if (carritoDTO.getItems() == null || carritoDTO.getItems().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        for (ItemCarritoDTO item : carritoDTO.getItems()) {
            Optional<Producto> productoOpt = productoRepositorio.findById(item.getProductoId());
            if (!productoOpt.isPresent()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            Producto producto = productoOpt.get();

            item.setPrecioUnitario(producto.getPrecio());

            Carrito carrito = new Carrito();
            carrito.setClienteId(carritoDTO.getClienteId());
            carrito.setProductoId(producto.getId());
            carrito.setProducto(producto.getNombre()); // Asumiendo que `Producto` tiene un `getNombre()`
            carrito.setCantidad(item.getCantidad());
            carrito.setPrecioUnitario(item.getPrecioUnitario());

            carritoItemsGuardados.add(carritoRepositorio.save(carrito));
        }

        return new ResponseEntity<>(carritoItemsGuardados, HttpStatus.CREATED);
    }

    @Operation(summary = "Obtener productos del carrito por cliente", description = "Recupera todos los ítems actuales en el carrito de compras de un cliente específico.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Ítems del carrito obtenidos exitosamente.",
                     content = @Content(mediaType = "application/json",
                                        schema = @Schema(implementation = Carrito.class))), // Retorna una lista de Carrito
        @ApiResponse(responseCode = "404", description = "Carrito vacío o cliente no encontrado.")
    })
    @GetMapping("/{clienteId}")
    public ResponseEntity<List<Carrito>> obtenerPorCliente(
            @Parameter(description = "ID del cliente para buscar su carrito.", required = true) @PathVariable Long clienteId) {
        List<Carrito> carrito = carritoRepositorio.findByClienteId(clienteId);
        if (carrito.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(carrito, HttpStatus.OK);
    }

    @Transactional
    @Operation(summary = "Confirmar compra y vaciar carrito", description = "Procesa los ítems en el carrito de un cliente, registra la venta y luego vacía el carrito de ese cliente.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Compra confirmada exitosamente."),
        @ApiResponse(responseCode = "400", description = "El carrito del cliente está vacío."),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor al procesar la compra.")
    })
    @PostMapping("/confirmar/{clienteId}")
    public ResponseEntity<String> confirmarCompra(
            @Parameter(description = "ID del cliente que confirma la compra.", required = true) @PathVariable Long clienteId) {
        List<Carrito> carritoItems = carritoRepositorio.findByClienteId(clienteId);

        if (carritoItems.isEmpty()) {
            return new ResponseEntity<>("El carrito está vacío para el cliente " + clienteId + ".", HttpStatus.BAD_REQUEST);
        }

        List<ItemCarritoDTO> itemsParaVenta = new ArrayList<>();
        for (Carrito item : carritoItems) {
            ItemCarritoDTO itemDto = new ItemCarritoDTO();
            itemDto.setProductoId(item.getProductoId());
            itemDto.setCantidad(item.getCantidad());
            itemDto.setPrecioUnitario(item.getPrecioUnitario());
            itemsParaVenta.add(itemDto);
        }

        CarritoDTO carritoParaServicio = new CarritoDTO(clienteId, itemsParaVenta);

        // Asumiendo que ventaServicio.registrarVenta maneja la lógica de negocio y excepciones
        ventaServicio.registrarVenta(carritoParaServicio);

        carritoRepositorio.deleteByClienteId(clienteId);

        return new ResponseEntity<>("Compra confirmada exitosamente para el cliente " + clienteId + ".", HttpStatus.OK);
    }
}