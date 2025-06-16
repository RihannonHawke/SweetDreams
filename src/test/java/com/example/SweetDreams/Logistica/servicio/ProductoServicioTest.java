package com.example.SweetDreams.logistica.servicio; // PAQUETE CORREGIDO CON 'L' MAYÚSCULA

import com.example.SweetDreams.logistica.modelo.Producto; // Importar la clase Producto
import com.example.SweetDreams.logistica.repositorio.ProductoRepositorio;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ProductoServicioTest {

    @Mock
    private ProductoRepositorio productoRepositorio; // Mock del repositorio de Producto

    @InjectMocks
    private ProductoServicio productoServicio; // El servicio de Producto que vamos a probar

    @BeforeEach
    void setUp() {
        // Inicializa los mocks antes de cada test
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCrearProducto() {
        // Arrange
        // Usamos el constructor de Lombok @AllArgsConstructor para Producto
        // CORREGIDO: Quitado las etiquetas de argumento (id:, nombre:, etc.)
        // Asumiendo que el constructor de Producto es (Long id, String nombre, String descripcion, Double precio, Integer stock, String categoria)
        Producto nuevoProducto = new Producto(null, "Laptop Gamer", "Portátil de alta gama", 1500.0, 10, "Electrónica");

        // Simula el comportamiento del repositorio cuando se guarda un producto
        // CORREGIDO: Quitado las etiquetas de argumento
        Producto productoGuardado = new Producto(1L, "Laptop Gamer", "Portátil de alta gama", 1500.0, 10, "Electrónica");
        when(productoRepositorio.save(any(Producto.class))).thenReturn(productoGuardado);

        // Act
        Producto resultado = productoServicio.crearProducto(nuevoProducto);

        // Assert
        assertNotNull(resultado, "El producto guardado no debería ser nulo");
        assertEquals(1L, resultado.getId(), "El ID del producto debería ser el esperado");
        assertEquals("Laptop Gamer", resultado.getNombre(), "El nombre del producto debería coincidir");
        verify(productoRepositorio, times(1)).save(nuevoProducto); // Verifica que save fue llamado
    }

    @Test
    void testGetAllProductos() {
        // Arrange
        Producto prod1 = new Producto(1L, "Teclado Mecánico", "Teclado gaming retroiluminado", 80.0, 50, "Periféricos");
        Producto prod2 = new Producto(2L, "Mouse Inalámbrico", "Mouse ergonómico", 30.0, 100, "Periféricos");
        List<Producto> productos = Arrays.asList(prod1, prod2);

        when(productoRepositorio.findAll()).thenReturn(productos);

        // Act
        List<Producto> resultado = productoServicio.getAllProductos();

        // Assert
        assertNotNull(resultado, "La lista de productos no debería ser nula");
        assertEquals(2, resultado.size(), "Debería devolver 2 productos");
        assertEquals("Teclado Mecánico", resultado.get(0).getNombre());
        assertEquals("Mouse Inalámbrico", resultado.get(1).getNombre());
        verify(productoRepositorio, times(1)).findAll();
    }

    @Test
    void testGetProductoById() {
        // Arrange
        Producto productoExistente = new Producto(3L, "Monitor Curvo", "Monitor de 27 pulgadas", 300.0, 20, "Monitores");

        when(productoRepositorio.findById(3L)).thenReturn(Optional.of(productoExistente));
        when(productoRepositorio.findById(99L)).thenReturn(Optional.empty()); // Simula no encontrado

        // Act
        Optional<Producto> encontrado = productoServicio.getProductoById(3L);
        Optional<Producto> noEncontrado = productoServicio.getProductoById(99L);

        // Assert
        assertTrue(encontrado.isPresent(), "El producto con ID 3 debería ser encontrado");
        assertEquals("Monitor Curvo", encontrado.get().getNombre());
        assertTrue(noEncontrado.isEmpty(), "El producto con ID 99 no debería ser encontrado");

        verify(productoRepositorio, times(1)).findById(3L);
        verify(productoRepositorio, times(1)).findById(99L);
    }

    @Test
    void testActualizarProducto() {
        // Arrange
        Producto productoActualizado = new Producto(4L, "Auriculares Bluetooth", "Auriculares con cancelación de ruido", 120.0, 30, "Audio");
        Producto productoModificado = new Producto(4L, "Auriculares Bluetooth Pro", "Auriculares con cancelación de ruido y estuche", 130.0, 25, "Audio");

        when(productoRepositorio.save(any(Producto.class))).thenReturn(productoModificado);

        // Act
        Producto resultado = productoServicio.actualizarProducto(productoModificado);

        // Assert
        assertNotNull(resultado);
        assertEquals(4L, resultado.getId());
        assertEquals("Auriculares Bluetooth Pro", resultado.getNombre());
        assertEquals(25, resultado.getStock());
        verify(productoRepositorio, times(1)).save(productoModificado);
    }

    @Test
    void testBorrarProducto() {
        // Arrange
        Long idBorrar = 5L;
        doNothing().when(productoRepositorio).deleteById(idBorrar); // No hace nada al llamar deleteById

        // Act
        productoServicio.borrarProducto(idBorrar);

        // Assert
        verify(productoRepositorio, times(1)).deleteById(idBorrar); // Verifica que deleteById fue llamado
    }
}