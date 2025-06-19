package dev.kkarrasmil80.gestoritv.cliente.error

/**
 *
 */

sealed class ClienteError(val message: String) {
    class ClienteNotFound(message: String) : ClienteError(message)
    class ClienteServiceError(message: String) : ClienteError(message)
    class ClienteValidationError(message: String) : ClienteError(message)
}