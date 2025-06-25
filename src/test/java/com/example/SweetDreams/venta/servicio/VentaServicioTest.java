package com.example.SweetDreams.venta.servicio;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.example.SweetDreams.venta.dto.CarritoDTO;
import com.example.SweetDreams.venta.dto.ItemCarritoDTO;
import com.example.SweetDreams.venta.dto.ProductoDTO;
import com.example.SweetDreams.venta.modelo.Venta;
import com.example.SweetDreams.venta.repositorio.VentaRepositorio;


public class VentaServicioTest {

    @Mock
    private VentaRepositorio ventaRepositorio;

    // Si tu VentaServicio depende de ProductoServicioVenta para obtener precios/productos,
    // deberías descomentar y mockearlo:
    // @Mock
    // private ProductoServicioVenta productoServicioVenta;

    @InjectMocks
    private VentaServicio ventaServicio;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegistrarVenta() {
        // 1. Configuración de datos de prueba (Arrange)

        // ProductoDTO ahora acepta (id, nombre, precio(Double), stock)
        ProductoDTO producto1 = new ProductoDTO(1L, "Producto A", 100.00, 10);
        ProductoDTO producto2 = new ProductoDTO(2L, "Producto B", 50.00, 20);

        // ItemCarritoDTO ahora acepta (productoId, cantidad)
        ItemCarritoDTO item1 = new ItemCarritoDTO(producto1.getId(), 2);
        ItemCarritoDTO item2 = new ItemCarritoDTO(producto2.getId(), 1);

        // ¡IMPORTANTE! Asignar el precio unitario en el ItemCarritoDTO para que el servicio lo use
        item1.setPrecioUnitario(producto1.getPrecio()); // 100.00
        item2.setPrecioUnitario(producto2.getPrecio()); // 50.00

        List<ItemCarritoDTO> itemsCarrito = Arrays.asList(item1, item2);

        // CarritoDTO ahora acepta (clienteId, items)
        CarritoDTO carritoDTO = new CarritoDTO(1L, itemsCarrito);

        // Simular el comportamiento del repositorio:
        Venta ventaGuardada = new Venta();
        ventaGuardada.setIdVenta(100L);
        ventaGuardada.setClienteId(carritoDTO.getClienteId());
        ventaGuardada.setFechaVenta(LocalDate.now());
        // El total calculado en el servicio será (2 * 100.00) + (1 * 50.00) = 250.00
        ventaGuardada.setTotal(250.00); // Asegúrate que tu Venta tiene un campo 'total' de tipo double
        ventaGuardada.setEstado("COMPLETADO"); // Asegúrate que tu Venta tiene un campo 'estado' de tipo String

        // Configurar el mock para que cuando se llame a save(), devuelva nuestra venta simulada
        when(ventaRepositorio.save(any(Venta.class))).thenReturn(ventaGuardada);

        // Si el VentaServicio necesita llamar a ProductoServicioVenta, mockéalo aquí.
        // Por ejemplo, si tu VentaServicio usa el productoId de ItemCarritoDTO para buscar el ProductoDTO completo:
        // when(productoServicioVenta.obtenerProductoPorId(1L)).thenReturn(Optional.of(producto1));
        // when(productoServicioVenta.obtenerProductoPorId(2L)).thenReturn(Optional.of(producto2));


        // 2. Ejecutar el método a probar (Act)
        // ¡Este es el método que queremos que tu VentaServicio tenga y procese!
        Venta resultadoVenta = ventaServicio.registrarVenta(carritoDTO);

        // 3. Verificar el resultado (Assert)
        assertNotNull(resultadoVenta, "La venta registrada no debería ser nula");
        assertEquals(100L, resultadoVenta.getIdVenta(), "El ID de la venta debería coincidir");
        assertEquals(carritoDTO.getClienteId(), resultadoVenta.getClienteId(), "El ID de cliente debe coincidir");
        assertEquals(250.00, resultadoVenta.getTotal(), 0.001, "El total de la venta debería ser 250.00"); // Usa delta para doubles
        assertEquals("COMPLETADO", resultadoVenta.getEstado(), "El estado de la venta debería ser COMPLETADO");
    }
}