package dev.kkarrasmil80.gestoritv.vehiculo.error

/**
 * Clase sellada que almacena todos los tipos de
 * errores que se podría dar en el programa.
 */

sealed class VehiculoError(val message: String) {
    class VehiculoServiceError(message: String) : VehiculoError(message)
    class VehiculoIdNotFound(message: String) : VehiculoError(message)
    class VehiculoStorageError(message: String) : VehiculoError(message)
    class VehiculoValidatorError(message: String) : VehiculoError(message)
}