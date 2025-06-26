package com.example.SweetDreams.logistica.servicio;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List; // Asegúrate de que Venta tenga los campos correctos para su constructor
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

import com.example.SweetDreams.logistica.modelo.Envio;
import com.example.SweetDreams.logistica.repositorio.EnvioRepositorio;
import com.example.SweetDreams.venta.modelo.Venta;

public class EnvioServicioTest {

    @Mock
    private EnvioRepositorio envioRepositorio;

    @InjectMocks
    private EnvioServicio envioServicio;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCrearEnvio() {
        // Arrange
        // CONSTRUCTORES AHORA SIN ETIQUETAS DE ARGUMENTO
        Envio nuevoEnvio = new Envio(null, 101L, "Calle Falsa 123", "Springfield", 5.50, "2025-06-15", 1234567890.0, "Pendiente");

        Envio envioGuardado = new Envio(1L, 101L, "Calle Falsa 123", "Springfield", 5.50, "2025-06-15", 1234567890.0, "Pendiente");
        when(envioRepositorio.save(any(Envio.class))).thenReturn(envioGuardado);

        // Act
        Envio resultado = envioServicio.crearEnvio(nuevoEnvio);

        // Assert
        assertNotNull(resultado, "El envío guardado no debería ser nulo");
        assertEquals(1L, resultado.getIdEnvio(), "El ID del envío debería ser el esperado");
        assertEquals("Springfield", resultado.getCiudadDestino(), "La ciudad destino debería coincidir");
        verify(envioRepositorio, times(1)).save(nuevoEnvio);
    }

    @Test
    void testGetAllEnvios() {
        // Arrange
        // CONSTRUCTORES AHORA SIN ETIQUETAS DE ARGUMENTO
        Envio envio1 = new Envio(1L, 101L, "Dir1", "Ciudad1", 10.0, "2025-06-01", 111111111.0, "En Proceso");
        Envio envio2 = new Envio(2L, 102L, "Dir2", "Ciudad2", 15.0, "2025-06-02", 222222222.0, "Entregado");
        List<Envio> envios = Arrays.asList(envio1, envio2);

        when(envioRepositorio.findAll()).thenReturn(envios);

        // Act
        List<Envio> resultado = envioServicio.getAllEnvios();

        // Assert
        assertNotNull(resultado, "La lista de envíos no debería ser nula");
        assertEquals(2, resultado.size(), "Debería devolver 2 envíos");
        assertEquals("Ciudad1", resultado.get(0).getCiudadDestino());
        assertEquals("Entregado", resultado.get(1).getEstado());
        verify(envioRepositorio, times(1)).findAll();
    }

    @Test
    void testGetEnvioById() {
        // Arrange
        // CONSTRUCTORES AHORA SIN ETIQUETAS DE ARGUMENTO
        Envio envioExistente = new Envio(3L, 103L, "Dir3", "Ciudad3", 20.0, "2025-06-03", 333333333.0, "Pendiente");

        when(envioRepositorio.findById(3L)).thenReturn(Optional.of(envioExistente));
        when(envioRepositorio.findById(99L)).thenReturn(Optional.empty());

        // Act
        Optional<Envio> encontrado = envioServicio.getEnvioById(3L);
        Optional<Envio> noEncontrado = envioServicio.getEnvioById(99L);

        // Assert
        assertTrue(encontrado.isPresent(), "El envío con ID 3 debería ser encontrado");
        assertEquals("Ciudad3", encontrado.get().getCiudadDestino());
        assertTrue(noEncontrado.isEmpty(), "El envío con ID 99 no debería ser encontrado");

        verify(envioRepositorio, times(1)).findById(3L);
        verify(envioRepositorio, times(1)).findById(99L);
    }

    @Test
    void testActualizarEnvio() {
        // Arrange
        // CONSTRUCTORES AHORA SIN ETIQUETAS DE ARGUMENTO
        Envio envioActualizado = new Envio(4L, 104L, "Dir4", "Ciudad4", 25.0, "2025-06-04", 444444444.0, "En Proceso");
        Envio envioModificado = new Envio(4L, 104L, "Nueva Dir4", "Nueva Ciudad4", 26.0, "2025-06-05", 444444444.0, "Entregado");

        when(envioRepositorio.save(any(Envio.class))).thenReturn(envioModificado);

        // Act
        Envio resultado = envioServicio.actualizarEnvio(envioModificado);

        // Assert
        assertNotNull(resultado);
        assertEquals(4L, resultado.getIdEnvio());
        assertEquals("Entregado", resultado.getEstado());
        assertEquals("Nueva Dir4", resultado.getDireccionDestino());
        verify(envioRepositorio, times(1)).save(envioModificado);
    }

    @Test
    void testBorrarEnvio() {
        // Arrange
        Long idBorrar = 5L;
        doNothing().when(envioRepositorio).deleteById(idBorrar);

        // Act
        envioServicio.borrarEnvio(idBorrar);

        // Assert
        verify(envioRepositorio, times(1)).deleteById(idBorrar);
    }

    @Test
    void testAsignarIdVentaDesdeVentaEnEnvioObjeto() {
        // Arrange
        Venta venta = new Venta(1L, LocalDate.now(), 50.0, "Completada");
        venta.setIdVenta(10L);

        Envio envio = new Envio(null, null, "Direccion", "Ciudad", 10.0, "2025-01-01", 123.0, "Estado");

        // Act
        envio.asignarIdVentaDesdeVenta(venta);

        // Assert
        assertEquals(10L, envio.getIdVenta());
    }
}