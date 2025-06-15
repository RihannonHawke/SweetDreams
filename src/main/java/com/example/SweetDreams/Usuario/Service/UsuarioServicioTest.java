package com.example.SweetDreams.Usuario.Service;

import com.example.SweetDreams.Usuario.Model.Usuario;
import com.example.SweetDreams.Usuario.Repository.UsuarioRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UsuarioServicioTest {

    @Mock
    private UsuarioRepository usuarioRepository; // Mock del repositorio

    @InjectMocks
    private UsuarioService usuarioService; // El servicio que queremos probar

    @BeforeEach
    void setUp() {
        // Inicializa los mocks antes de cada test
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGuardarUsuario() {
        // 1. Configuración de datos de prueba (Arrange)
        Usuario usuarioAguardar = new Usuario("testuser", "test@example.com", "password123");
        // Simula el comportamiento del repositorio:
        // Cuando se llame a save() con cualquier objeto Usuario, devuelve el mismo Usuario
        // simulando que la base de datos le asigna un ID.
        Usuario usuarioGuardado = new Usuario("testuser", "test@example.com", "password123");
        usuarioGuardado.setId(1L); // Asigna un ID simulado para el resultado

        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioGuardado);

        // 2. Ejecutar el método a probar (Act)
        Usuario resultadoUsuario = usuarioService.guardarUsuario(usuarioAguardar);

        // 3. Verificar el resultado (Assert)
        assertNotNull(resultadoUsuario, "El usuario guardado no debería ser nulo");
        assertEquals(1L, resultadoUsuario.getId(), "El ID del usuario debería coincidir con el asignado");
        assertEquals("testuser", resultadoUsuario.getNombreUsuario(), "El nombre de usuario debería coincidir");
        assertEquals("test@example.com", resultadoUsuario.getEmail(), "El email debería coincidir");
        // No se verifica la contraseña directamente por seguridad, pero puedes verificar que se haya pasado
        assertNotNull(resultadoUsuario.getPassword(), "La contraseña no debería ser nula");

        // Opcional: Verifica que el método save del repositorio fue llamado exactamente una vez
        verify(usuarioRepository, times(1)).save(usuarioAguardar);
    }

    @Test
    void testGetAllUsuarios() {
        // Arrange
        Usuario usuario1 = new Usuario("user1", "user1@example.com", "pass1");
        usuario1.setId(1L);
        Usuario usuario2 = new Usuario("user2", "user2@example.com", "pass2");
        usuario2.setId(2L);
        List<Usuario> usuarios = Arrays.asList(usuario1, usuario2);

        when(usuarioRepository.findAll()).thenReturn(usuarios);

        // Act
        List<Usuario> resultado = usuarioService.getAllUsuarios();

        // Assert
        assertNotNull(resultado, "La lista de usuarios no debería ser nula");
        assertEquals(2, resultado.size(), "Debería devolver 2 usuarios");
        assertEquals("user1", resultado.get(0).getNombreUsuario());
        assertEquals("user2", resultado.get(1).getNombreUsuario());

        verify(usuarioRepository, times(1)).findAll();
    }

    @Test
    void testGetUsuarioById() {
        // Arrange
        Usuario usuario = new Usuario("userById", "user@id.com", "passById");
        usuario.setId(5L);

        when(usuarioRepository.findById(5L)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty()); // Para un ID no encontrado

        // Act
        Optional<Usuario> encontrado = usuarioService.getUsuarioById(5L);
        Optional<Usuario> noEncontrado = usuarioService.getUsuarioById(99L);

        // Assert
        assertTrue(encontrado.isPresent(), "El usuario con ID 5 debería ser encontrado");
        assertEquals("userById", encontrado.get().getNombreUsuario());
        assertTrue(noEncontrado.isEmpty(), "El usuario con ID 99 no debería ser encontrado");

        verify(usuarioRepository, times(1)).findById(5L);
        verify(usuarioRepository, times(1)).findById(99L);
    }

    @Test
    void testEliminarUsuario() {
        // Arrange
        Long idAEliminar = 1L;
        // Mockear el comportamiento de deleteById para que no haga nada cuando se le llame
        doNothing().when(usuarioRepository).deleteById(idAEliminar);

        // Act
        usuarioService.eliminarUsuario(idAEliminar);

        // Assert
        // Verificar que el método deleteById del repositorio fue llamado exactamente una vez con el ID correcto
        verify(usuarioRepository, times(1)).deleteById(idAEliminar);
    }
}