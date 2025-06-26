package com.example.SweetDreams.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import static org.springframework.security.config.Customizer.withDefaults;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager; // Para usar AbstractHttpConfigurer::disable
import org.springframework.security.web.SecurityFilterChain; // Para usar withDefaults()

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
@EnableWebSecurity // Habilita la configuración de seguridad web de Spring
public class SecurityConfig {

    // --- Configuración de Spring Security ---

    // Define un codificador de contraseñas (PasswordEncoder)
    // Es crucial para guardar contraseñas de forma segura (hasheada)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // BCrypt es un algoritmo de hashing fuerte
    }

    // Configura un usuario en memoria con tu nombre de usuario y contraseña
    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        // La contraseña debe estar codificada. Aquí "miContrasenaSegura123" es la contraseña
        // que usarás, y passwordEncoder.encode() la codifica.
        UserDetails user = User.builder()
            .username("admin") // Tu nombre de usuario deseado
            .password(passwordEncoder.encode("admin"))
            .roles("ADMIN", "USER") // Roles que tendrá este usuario
            .build();
        return new InMemoryUserDetailsManager(user);
    }

    // Configura las reglas de seguridad HTTP
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Deshabilita CSRF para APIs REST (si no lo vas a usar explícitamente con tokens CSRF)
            .csrf(AbstractHttpConfigurer::disable)
            // Configura las reglas de autorización para las solicitudes HTTP
            .authorizeHttpRequests(authorize -> authorize
                // Permite el acceso público a las rutas de Swagger UI y la documentación OpenAPI
                .requestMatchers("/v3/api-docs/**").permitAll() // Para el JSON de OpenAPI
                .requestMatchers("/swagger-ui.html").permitAll() // La página principal de Swagger UI
                .requestMatchers("/swagger-ui/**").permitAll() // Recursos estáticos de Swagger UI
                .requestMatchers("/doc/**").permitAll() // Si estás usando el prefijo /doc/ para Swagger UI
                // Asegúrate de que tus endpoints API necesiten autenticación
                // Cualquier otra solicitud requiere autenticación (pedirá usuario/contraseña)
                .anyRequest().authenticated()
            )
            // Configura la autenticación básica HTTP (aparecerá un popup de login en el navegador)
            .httpBasic(withDefaults());

        return http.build();
    }

    // --- Configuración de Swagger/OpenAPI Metadata ---

    // Define la información general de tu API que se mostrará en Swagger UI
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Sweet Dreams API")
                        .version("1.0.0")
                        .description("API para la gestión de datos de productos, usuarios, carritos y ventas en SweetDreams."));
    }
}