package dev.kkarrasmil80.gestoritv.cliente.error

/**
 *
 */

// Errores relacionados con operaciones y validaciones del cliente
sealed class ClienteError(val message: String) {
    // No se encontró el cliente solicitado
    class ClienteNotFound(message: String) : ClienteError(message)
    // Error general del servicio cliente
    class ClienteServiceError(message: String) : ClienteError(message)
    // Error general del servicio cliente
    class ClienteValidationError(message: String) : ClienteError(message)
}
