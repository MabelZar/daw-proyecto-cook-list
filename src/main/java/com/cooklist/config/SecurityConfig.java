package com.cooklist.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.cooklist.middlewares.CustomUserDetailsService;
import com.cooklist.middlewares.JwtAuthenticationFilter;
import com.cooklist.util.DataConstant;
import com.cooklist.util.MessageConstant;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final SecurityErrorHandler securityErrorHandler;

    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter,
            CustomUserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder,
            SecurityErrorHandler securityErrorHandler) {

        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.securityErrorHandler = securityErrorHandler;
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {

        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);

        authProvider.setPasswordEncoder(passwordEncoder);

        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationProvider authenticationProvider) {
        return new ProviderManager(authenticationProvider);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Los errores de seguridad (401/403) ocurren antes de los controladores:
                // se escriben en formato RFC 9457 directamente desde aquí
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint((request, response, authException) -> securityErrorHandler.write(
                                request,
                                response,
                                HttpStatus.UNAUTHORIZED,
                                DataConstant.ERROR_TYPE_UNAUTHORIZED,
                                MessageConstant.NOT_AUTHENTICATED,
                                MessageConstant.AUTH_REQUIRED_TO_ACCESS_RESOURCE))
                        .accessDeniedHandler((request, response, accessDeniedException) -> securityErrorHandler.write(
                                request,
                                response,
                                HttpStatus.FORBIDDEN,
                                DataConstant.ERROR_TYPE_ACCESS_DENIED,
                                MessageConstant.ACCESS_DENIED,
                                MessageConstant.DONT_HAVE_PERMISSIONS_TO_THIS_ACTION)))
                .authenticationProvider(authenticationProvider())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/error").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/logout").authenticated()
                        .requestMatchers(HttpMethod.POST, "/users/create-customer").permitAll()
                        .requestMatchers(HttpMethod.GET, "/users/find-current").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/users/update-current").authenticated()
                        .requestMatchers("/users/**").hasRole(DataConstant.ADMIN_ROLE)
                        .requestMatchers("/roles/**").hasRole(DataConstant.ADMIN_ROLE)
                        // Swagger UI y OpenAPI (rutas relativas al servlet, sin context-path)
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                        .anyRequest().authenticated())
                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}