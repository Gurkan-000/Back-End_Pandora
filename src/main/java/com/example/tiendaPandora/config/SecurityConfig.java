package com.example.tiendaPandora.config;

import com.example.tiendaPandora.security.jwt.JwtEntryPoint;
import com.example.tiendaPandora.security.jwt.JwtFilter;
import jakarta.servlet.DispatcherType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.XorCsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        .csrfTokenRequestHandler(new XorCsrfTokenRequestAttributeHandler())
                )
                .authorizeHttpRequests(auth -> auth
                        .dispatcherTypeMatchers(DispatcherType.ERROR).permitAll()
                        .requestMatchers("/error").permitAll()
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers(HttpMethod.GET,
                                "/api/categoria/**",
                                "/api/subcategoria/**",
                                "/api/atributo/**",
                                "/api/valor-atributo/**",
                                "/api/marca/**",
                                "/api/producto/**",
                                "/api/imagen/**"
                        ).permitAll()
                        .requestMatchers(
                                "/api/categoria/**",
                                "/api/subcategoria/**",
                                "/api/atributo/**",
                                "/api/valor-atributo/**",
                                "/api/marca/**",
                                "/api/producto/**",
                                "/api/variante-producto",
                                "/api/imagen/**"
                        ).hasRole("ADMIN")
                        .requestMatchers("/api/dashboard/**").hasRole("ADMIN")
                        .requestMatchers("/api/carrito/**").hasAnyRole("CLIENTE","ADMIN")
                        .requestMatchers("/api/pasarela-pago/comprar").hasRole("CLIENTE")
                        .requestMatchers(HttpMethod.GET, "/api/pedido/**").hasAnyRole("CLIENTE", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/pedido/**").hasRole("CLIENTE")
                        .requestMatchers(HttpMethod.PATCH, "/api/pedido/enviar/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/pedido/entregar/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .exceptionHandling(exception -> exception.authenticationEntryPoint(jwtEntryPoint()))
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return  http.build();
    }

    @Bean
    public JwtEntryPoint jwtEntryPoint() {
        return new JwtEntryPoint();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(
                List.of("http://localhost:4200")
        );
        configuration.setAllowCredentials(true);
        configuration.setAllowedMethods(
                Arrays.asList(
                        "GET",
                        "POST",
                        "PUT",
                        "DELETE",
                        "OPTIONS",
                        "PATCH"
                )
        );
        configuration.setAllowedHeaders(
                Arrays.asList(
                        "Content-Type",
                        "Authorization",
                        "X-XSRF-TOKEN"
                )
        );
        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
