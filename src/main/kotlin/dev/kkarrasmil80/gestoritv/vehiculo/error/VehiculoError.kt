package dev.kkarrasmil80.gestoritv.vehiculo.error

/**
 * Errores relacionados con operaciones sobre Vehículo.
 */
sealed class VehiculoError(val message: String) {

    // Error general en el servicio de vehículo
    class VehiculoServiceError(message: String) : VehiculoError(message)

    // Error cuando no se encuentra un vehículo por ID
    class VehiculoIdNotFound(message: String) : VehiculoError(message)

    // Error al acceder o manipular el almacenamiento de datos
    class VehiculoStorageError(message: String) : VehiculoError(message)

    // Error de validación en los datos del vehículo
    class VehiculoValidatorError(message: String) : VehiculoError(message)
}
