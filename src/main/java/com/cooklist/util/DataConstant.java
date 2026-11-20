package com.cooklist.util;

public final class DataConstant {

    public final static String ADMIN_ROLE = "ADMIN";
    public final static String CLIENT_ROLE = "CLIENT";

    // URI base para el campo "type" de las respuestas de error (Problem Details)
    public final static String ERROR_TYPE_BASE_URI = "http://localhost:8080/errors/";

    // Tipos de error que se usan en las respuestas Problem Details
    public final static String ERROR_TYPE_NOT_FOUND = "not-found";
    public final static String ERROR_TYPE_VALIDATION_FAILED = "validation-failed";
    public final static String ERROR_TYPE_UNAUTHORIZED = "unauthorized";
    public final static String ERROR_TYPE_ACCESS_DENIED = "access-denied";
    public final static String ERROR_TYPE_CONFLICT = "conflict";
    public final static String ERROR_TYPE_INTERNAL_ERROR = "internal-error";
}
