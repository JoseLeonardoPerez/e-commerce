package com.joseLeo.ecommerce.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth
                        // ✅ Páginas públicas (acceso libre)
                        .requestMatchers(
                                "/", "/index", "/index.html",
                                "/tienda", "/tienda.html",
                                "/senuelosycarnadas", "/senuelosycarnadas.html",
                                "/css/**", "/js/**", "/images/**",
                                "/login.html", "/register.html", "/error",
                                "/auth/register"
                        ).permitAll()

                        // ✅ Carrito, checkout y API requieren autenticación
                        .requestMatchers(
                                "/carrito/**",
                                "/checkout/**",
                                "/orders/**",
                                "/order",
                                "/order.html",
                                "/order-summary.html",
                                "/success.html"
                        ).authenticated()

                        // 🔒 cualquier otra ruta pedirá login
                        .anyRequest().authenticated()
                )

                // Form login para web
                .formLogin(form -> form
                        .loginPage("/login.html")
                        .loginProcessingUrl("/perform_login")
                        .defaultSuccessUrl("/index.html", true)
                        .failureUrl("/login.html?error=true")
                        .permitAll()
                )

                // Basic Auth para REST API
                .httpBasic()

                // Logout
                .and()
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login.html?logout=true")
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
