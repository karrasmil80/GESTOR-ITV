package dev.kkarrasmil80.gestoritv.cita.validator

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import dev.kkarrasmil80.gestoritv.cita.dao.CitaEntity
import dev.kkarrasmil80.gestoritv.cita.error.CitaError
import dev.kkarrasmil80.gestoritv.cita.models.Cita
import dev.kkarrasmil80.gestoritv.vehiculo.error.VehiculoError

class CitaValidator : ValidatorCita<Cita, CitaError> {
    override fun validate(t: Cita): Result<Cita, CitaError> {
        if (t.vehiculo == null) {
            return Err(CitaError.CitaValidatorError("El vehiculo no puede ser nulo"))
        }

        if (t.fechaCita.isBlank()) {
            return Err(CitaError.CitaValidatorError("La fecha no puede estar en blanco"))
        }

        if (t.hora.isBlank()) {
            return Err(CitaError.CitaValidatorError("La hora no puede estar en blanco"))
        }

        return Ok(t)
    }

}