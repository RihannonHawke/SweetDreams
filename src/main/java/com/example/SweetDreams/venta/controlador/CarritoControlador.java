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

@RestController
@RequestMapping("/carrito")
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

    // Agregar productos al carrito con DTO
    @PostMapping
    public ResponseEntity<List<Carrito>> agregar(@RequestBody CarritoDTO carritoDTO) {
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
            carrito.setProducto(producto.getNombre());
            carrito.setCantidad(item.getCantidad());
            carrito.setPrecioUnitario(item.getPrecioUnitario());

            carritoItemsGuardados.add(carritoRepositorio.save(carrito));
        }

        return new ResponseEntity<>(carritoItemsGuardados, HttpStatus.CREATED);
    }

    // Obtener productos del carrito por cliente
    @GetMapping("/{clienteId}")
    public ResponseEntity<List<Carrito>> obtenerPorCliente(@PathVariable Long clienteId) {
        List<Carrito> carrito = carritoRepositorio.findByClienteId(clienteId);
        if (carrito.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(carrito, HttpStatus.OK);
    }

    @Transactional
    @PostMapping("/confirmar/{clienteId}")
    public ResponseEntity<String> confirmarCompra(@PathVariable Long clienteId) {
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

        ventaServicio.registrarVenta(carritoParaServicio);

        carritoRepositorio.deleteByClienteId(clienteId);

        return new ResponseEntity<>("Compra confirmada exitosamente para el cliente " + clienteId + ".", HttpStatus.OK);
    }
}