package com.cooklist.util;

public final class MessageConstant {
    public final static String DONT_HAVE_PERMISSIONS_TO_THIS_ACTION = "No tienes permisos para ejecutar esta acción.";
    public final static String USER_DOESNT_EXIST = "El usuario no existe.";
    public final static String CATEGORY_DOESNT_EXIST = "La categoría no existe.";
    public final static String RECIPE_DOESNT_EXIST = "La receta no existe.";
    public final static String INGREDIENT_DOESNT_EXIST = "El ingrediente no existe.";
    public final static String ROLE_DOESNT_EXIST = "El rol no existe.";
    public final static String EMAIL_EXISTS = "El correo electrónico ya está registrado.";
    public final static String CLIENT_ROLE_DOESNT_EXIST = "No existe el rol CLIENT en la base de datos.";

    // Mensajes del manejador global de excepciones y de Spring Security
    public final static String RESOURCE_NOT_FOUND_TITLE = "Recurso no encontrado";
    public final static String VALIDATION_ERROR_TITLE = "Error de Validación de Parámetros";
    public final static String VALIDATION_ERROR_DETAIL = "Uno o varios campos no cumplen las restricciones.";
    public final static String INVALID_VALUE = "Valor no válido";
    public final static String BAD_CREDENTIALS = "Credenciales incorrectas.";
    public final static String NOT_AUTHENTICATED = "No autenticado";
    public final static String AUTH_REQUIRED_TO_ACCESS_RESOURCE = "Es necesario autenticarse para acceder a este recurso.";
    public final static String ACCESS_DENIED = "Acceso denegado";
    public final static String DONT_HAVE_PERMISSIONS_TO_THIS_OPERATION = "No tienes permisos para realizar esta operación.";
    public final static String DATA_CONFLICT_TITLE = "Conflicto de datos";
    public final static String DATA_CONFLICT_DETAIL = "La operación viola una restricción de la base de datos.";
    public final static String INTERNAL_ERROR_TITLE = "Error interno del servidor";
    public final static String INTERNAL_ERROR_DETAIL = "Se ha producido un error inesperado.";
    public final static String TOKEN_DURATION_INVALID = "La duración del token debe ser superior a cero y no superar las dos horas.";
}
