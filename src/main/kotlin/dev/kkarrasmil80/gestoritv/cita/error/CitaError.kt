package dev.kkarrasmil80.gestoritv.cita.error

import dev.kkarrasmil80.gestoritv.vehiculo.error.VehiculoError

// Errores específicos para el manejo de citas en la aplicación
sealed class CitaError(val message: String) {
    // Error general relacionado con la lógica del servicio de citas
    class CitaServiceError(message: String) : CitaError(message)

    // Error que ocurre en el repositorio al acceder o manipular datos
    class CitaRepositoryError(message: String) : CitaError(message)

    // Error cuando no se encuentra una cita con el ID especificado
    class CitaIdNotFound(message: String) : CitaError(message)

    // Error relacionado con el almacenamiento (persistencia) de las citas
    class CitaStorageError(message: String) : CitaError(message)

    // Error que indica que la validación de una cita ha fallado (datos inválidos)
    class CitaValidatorError(message: String) : CitaError(message)
}
