package dev.kkarrasmil80.gestoritv.vehiculo.error

/**
 *
 */

sealed class VehiculoError(val message: String) {
    class VehiculoServiceError(message: String) : VehiculoError(message)
    class VehiculoIdNotFound(message: String) : VehiculoError(message)
    class VehiculoStorageError(message: String) : VehiculoError(message)
    class VehiculoValidatorError(message: String) : VehiculoError(message)
}