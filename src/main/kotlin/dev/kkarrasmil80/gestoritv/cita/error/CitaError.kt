package dev.kkarrasmil80.gestoritv.cita.error

import dev.kkarrasmil80.gestoritv.vehiculo.error.VehiculoError

/**
 *
 */

sealed class CitaError(message: String) {
    class CitaServiceError(message: String) : CitaError(message)
    class CitaRepositoryError(message: String) : CitaError(message)
    class CitaIdNotFound(message: String) : CitaError(message)
    class CitaStorageError(message: String) : CitaError(message)
    class CitaValidatorError(message: String) : CitaError(message)
}