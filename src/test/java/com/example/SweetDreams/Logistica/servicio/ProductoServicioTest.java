package com.example.SweetDreams.Logistica.servicio;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.example.SweetDreams.logistica.modelo.Producto;
import com.example.SweetDreams.logistica.repositorio.ProductoRepositorio;

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
        // Asegúrate de que los tipos de datos coincidan con los de tu clase Producto.java
// Por ejemplo, si 'precio' es Double, 1500.0 es correcto. Si fuera Long, sería 1500L.
        Producto nuevoProducto = new Producto(null, "Laptop Gamer", "Portátil de alta gama", 1500.0);

        // Simula el comportamiento del repositorio cuando se guarda un producto
        Producto productoGuardado = new Producto(1L, "Laptop Gamer", "Portátil de alta gama", 1500.0);
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
    void testGetAllProducts() {
        // Arrange
        Producto producto1 = new Producto(1L, "Mouse Gaming", "Mouse con RGB", 50.0);
        Producto producto2 = new Producto(2L, "Teclado Mecánico", "Teclado retroiluminado", 120.0);
        List<Producto> productos = Arrays.asList(producto1, producto2);

        when(productoRepositorio.findAll()).thenReturn(productos);

        // Act
        List<Producto> resultado = productoServicio.getAllProducts();

        // Assert
        assertNotNull(resultado, "La lista de productos no debería ser nula");
        assertEquals(2, resultado.size(), "Debería devolver 2 productos");
        assertEquals("Mouse Gaming", resultado.get(0).getNombre());
        assertEquals("Teclado Mecánico", resultado.get(1).getNombre());
        verify(productoRepositorio, times(1)).findAll();
    }

    @Test
    void testGetProductById() {
        // Arrange
        Producto productoExistente = new Producto(3L, "Monitor Ultrawide", "Monitor curvo de 34 pulgadas", 700.0);

        when(productoRepositorio.findById(3L)).thenReturn(Optional.of(productoExistente));
        when(productoRepositorio.findById(99L)).thenReturn(Optional.empty()); // Simula no encontrado

        // Act
        Optional<Producto> encontrado = productoServicio.getProductById(3L);
        Optional<Producto> noEncontrado = productoServicio.getProductById(99L);

        // Assert
        assertTrue(encontrado.isPresent(), "El producto con ID 3 debería ser encontrado");
        assertEquals("Monitor Ultrawide", encontrado.get().getNombre());
        assertTrue(noEncontrado.isEmpty(), "El producto con ID 99 no debería ser encontrado");

        verify(productoRepositorio, times(1)).findById(3L);
        verify(productoRepositorio, times(1)).findById(99L);
    }

    @Test
    void testActualizarProducto() {
        // Arrange
        Producto productoOriginal = new Producto(4L, "Auriculares Inalámbricos", "Auriculares con cancelación de ruido", 200.0);
        Producto productoModificado = new Producto(4L, "Auriculares Inalámbricos Pro", "Auriculares con ANC mejorada", 250.0);

        when(productoRepositorio.save(any(Producto.class))).thenReturn(productoModificado);

        // Act
        Producto resultado = productoServicio.actualizarProducto(productoModificado);

        // Assert
        assertNotNull(resultado);
        assertEquals(4L, resultado.getId());
        assertEquals("Auriculares Inalámbricos Pro", resultado.getNombre());
        assertEquals(250.0, resultado.getPrecio());
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