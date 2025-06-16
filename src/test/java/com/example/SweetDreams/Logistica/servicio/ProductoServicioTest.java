package com.example.SweetDreams.logistica.servicio;

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
    private ProductoRepositorio productoRepositorio;

    @InjectMocks
    private ProductoServicio productoServicio;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCrearProducto() {
        // Arrange
        // CONSTRUCTORES AHORA SIN ETIQUETAS DE ARGUMENTO
        Producto nuevoProducto = new Producto(null, "Laptop Gamer", "Portátil de alta gama", 1500.0);

        Producto productoGuardado = new Producto(1L, "Laptop Gamer", "Portátil de alta gama", 1500.0);
        when(productoRepositorio.save(any(Producto.class))).thenReturn(productoGuardado);

        // Act
        Producto resultado = productoServicio.crearProducto(nuevoProducto);

        // Assert
        assertNotNull(resultado, "El producto guardado no debería ser nulo");
        assertEquals(1L, resultado.getId(), "El ID del producto debería ser el esperado");
        assertEquals("Laptop Gamer", resultado.getNombre(), "El nombre del producto debería coincidir");
        verify(productoRepositorio, times(1)).save(nuevoProducto);
    }

    @Test
    void testGetAllProductos() {
        // Arrange
        // CONSTRUCTORES AHORA SIN ETIQUETAS DE ARGUMENTO
        Producto prod1 = new Producto(1L, "Teclado Mecánico", "Teclado gaming retroiluminado", 80.0);
        Producto prod2 = new Producto(2L, "Mouse Inalámbrico", "Mouse ergonómico", 30.0);
        List<Producto> productos = Arrays.asList(prod1, prod2);

        when(productoRepositorio.findAll()).thenReturn(productos);

        // Act
        List<Producto> resultado = productoServicio.getAllProducts();
        

        

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
        // CONSTRUCTORES AHORA SIN ETIQUETAS DE ARGUMENTO
        Producto productoExistente = new Producto(3L, "Monitor Curvo", "Monitor de 27 pulgadas", 300.0);

        when(productoRepositorio.findById(3L)).thenReturn(Optional.of(productoExistente));
        when(productoRepositorio.findById(99L)).thenReturn(Optional.empty());

        // Act
        Optional<Producto> encontrado = productoServicio.getProductById(3L);
        Optional<Producto> noEncontrado = productoServicio.getProductById(99L);

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
        // CONSTRUCTORES AHORA SIN ETIQUETAS DE ARGUMENTO
        Producto productoActualizado = new Producto(4L, "Auriculares Bluetooth", "Auriculares con cancelación de ruido", 120.0);
        Producto productoModificado = new Producto(4L, "Auriculares Bluetooth Pro", "Auriculares con cancelación de ruido y estuche", 130.0);

        when(productoRepositorio.save(any(Producto.class))).thenReturn(productoModificado);

        // Act
        Producto resultado = productoServicio.actualizarProducto(productoModificado);

        // Assert
        assertNotNull(resultado);
        assertEquals(4L, resultado.getId());
        assertEquals("Auriculares Bluetooth Pro", resultado.getNombre());
        // La línea getStock() fue eliminada ya que no existe en tu clase Producto
        // assertEquals(25, resultado.getStock());
        verify(productoRepositorio, times(1)).save(productoModificado);
    }

    @Test
    void testBorrarProducto() {
        // Arrange
        Long idBorrar = 5L;
        doNothing().when(productoRepositorio).deleteById(idBorrar);

        // Act
        productoServicio.borrarProducto(idBorrar);

        // Assert
        verify(productoRepositorio, times(1)).deleteById(idBorrar);
    }
}