package com.cooklist.config;

import java.io.IOException;
import java.net.URI;
import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.stereotype.Component;

import com.cooklist.util.DataConstant;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Escribe las respuestas de error 401 y 403 en el mismo formato que
 * el GlobalExceptionHandler (Problem Details).
 *
 * Ojo: los errores de Spring Security saltan ANTES de llegar a los
 * controllers, así que el @RestControllerAdvice no los pilla y hay
 * que escribir la respuesta a mano desde aquí.
 */
@Component
public class SecurityErrorHandler {

    // Me creo mi propio ObjectMapper porque la app no tiene ninguno como bean.
    // El JavaTimeModule hace falta para serializar el Instant del timestamp.
    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    public SecurityErrorHandler() {
    }

    public void write(
            HttpServletRequest request,
            HttpServletResponse response,
            HttpStatus status,
            String type,
            String title,
            String detail) throws IOException {

        // ProblemDetail es lo que trae Spring para este formato de errores
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(status, detail);

        // "type" identifica el tipo de error con una URI
        problem.setType(URI.create(DataConstant.ERROR_TYPE_BASE_URI + type));
        problem.setTitle(title);

        // "instance" indica qué petición originó el error
        problem.setInstance(URI.create(request.getRequestURI()));

        // Campo extra que no es del estándar pero que al frontend le viene bien
        problem.setProperty("timestamp", Instant.now());

        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE);

        // Convierto el ProblemDetail a JSON y lo meto en el body de la respuesta
        objectMapper.writeValue(response.getOutputStream(), problem);
    }
}
