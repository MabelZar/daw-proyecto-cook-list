package com.cooklist.controller;

import java.net.URI;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.cooklist.util.DataConstant;
import com.cooklist.util.MessageConstant;
import com.cooklist.util.exception.ResourceNotFoundException;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Manejador de errores de toda la app. Con @RestControllerAdvice capturo
 * aquí las excepciones que se escapen de cualquier controller y devuelvo
 * siempre un JSON de error con el mismo formato (Problem Details).
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private final static Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /** Cuando no existe lo que se busca en la BD. */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleResourceNotFound(
            ResourceNotFoundException ex, HttpServletRequest request) {

        LOGGER.warn("Recurso no encontrado: {}", ex.getMessage());

        return buildProblem(HttpStatus.NOT_FOUND, DataConstant.ERROR_TYPE_NOT_FOUND,
                MessageConstant.RESOURCE_NOT_FOUND_TITLE, ex.getMessage(), request, null);
    }

    /** Fallo de @Valid en los DTO: devuelvo el error de cada campo. */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleValidation(
            MethodArgumentNotValidException ex, HttpServletRequest request) {

        LOGGER.warn("Validación fallida: {}", ex.getMessage());

        // Lista de errores campo a campo para que el frontend los muestre
        List<Map<String, String>> errors = new ArrayList<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errors.add(Map.of(
                    "field", fieldError.getField(),
                    "message", Objects.requireNonNullElse(fieldError.getDefaultMessage(), MessageConstant.INVALID_VALUE)));
        }

        return buildProblem(HttpStatus.UNPROCESSABLE_ENTITY, DataConstant.ERROR_TYPE_VALIDATION_FAILED,
                MessageConstant.VALIDATION_ERROR_TITLE,
                MessageConstant.VALIDATION_ERROR_DETAIL, request, errors);
    }

    /** Login con email o contraseña incorrectos. */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ProblemDetail> handleBadCredentials(
            BadCredentialsException ex, HttpServletRequest request) {

        LOGGER.warn("Intento de login con credenciales incorrectas");

        return buildProblem(HttpStatus.UNAUTHORIZED, DataConstant.ERROR_TYPE_UNAUTHORIZED,
                MessageConstant.NOT_AUTHENTICATED, MessageConstant.BAD_CREDENTIALS, request, null);
    }

    /** Usuario logueado pero sin el rol que pide el @PreAuthorize. */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ProblemDetail> handleAccessDenied(
            AccessDeniedException ex, HttpServletRequest request) {

        LOGGER.warn("Acceso denegado en {}", request.getRequestURI());

        return buildProblem(HttpStatus.FORBIDDEN, DataConstant.ERROR_TYPE_ACCESS_DENIED,
                MessageConstant.ACCESS_DENIED,
                MessageConstant.DONT_HAVE_PERMISSIONS_TO_THIS_OPERATION, request, null);
    }

    /** Problemas de BD: duplicados, restricciones, etc. */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ProblemDetail> handleDataIntegrity(
            DataIntegrityViolationException ex, HttpServletRequest request) {

        LOGGER.warn("Conflicto con la base de datos: {}", ex.getMostSpecificCause().getMessage());

        return buildProblem(HttpStatus.CONFLICT, DataConstant.ERROR_TYPE_CONFLICT,
                MessageConstant.DATA_CONFLICT_TITLE,
                MessageConstant.DATA_CONFLICT_DETAIL, request, null);
    }

    /** Red de seguridad por si se me escapa algo: nunca muestro el error interno al cliente. */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleGeneric(Exception ex, HttpServletRequest request) {

        LOGGER.error(ex.getMessage(), ex);

        return buildProblem(HttpStatus.INTERNAL_SERVER_ERROR, DataConstant.ERROR_TYPE_INTERNAL_ERROR,
                MessageConstant.INTERNAL_ERROR_TITLE,
                MessageConstant.INTERNAL_ERROR_DETAIL, request, null);
    }

    /** Monta la respuesta de error que comparten todos los handlers de arriba. */
    private ResponseEntity<ProblemDetail> buildProblem(
            HttpStatus status, String type, String title, String detail,
            HttpServletRequest request, Object errors) {

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(status, detail);

        problem.setType(URI.create(DataConstant.ERROR_TYPE_BASE_URI + type));
        problem.setTitle(title);
        problem.setInstance(URI.create(request.getRequestURI()));
        problem.setProperty("timestamp", Instant.now());

        // "errors" solo aparece en validaciones (422)
        if (errors != null) {
            problem.setProperty("errors", errors);
        }

        return ResponseEntity.status(status)
                .contentType(MediaType.APPLICATION_PROBLEM_JSON)
                .body(problem);
    }
}
