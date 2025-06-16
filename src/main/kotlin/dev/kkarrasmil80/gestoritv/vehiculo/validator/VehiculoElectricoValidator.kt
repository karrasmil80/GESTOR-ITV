package dev.kkarrasmil80.gestoritv.vehiculo.validator

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import dev.kkarrasmil80.gestoritv.vehiculo.error.VehiculoError
import dev.kkarrasmil80.gestoritv.vehiculo.models.VehiculoElectrico

class VehiculoElectricoValidator : Validator<VehiculoElectrico, VehiculoError> {
    override fun validate(t: VehiculoElectrico): Result<VehiculoElectrico, VehiculoError> {
        if (t.id <= 0) {
            return Err(VehiculoError.VehiculoValidatorError("ID inválido"))
        }

        if (t.matricula.isBlank()) {
            return Err(VehiculoError.VehiculoValidatorError("Matrícula inválida"))
        }

        if (!t.matricula.matches(Regex("[0-9]{4}-[A-Z]{3}"))){
            return Err(VehiculoError.VehiculoValidatorError("La matricula debe de seguir el patron especificado"))
        }

        if (t.modelo.isBlank()) {
            return Err(VehiculoError.VehiculoValidatorError("Modelo inválido"))
        }

        if (t.anio > 2025) {
            return Err(VehiculoError.VehiculoValidatorError("El vehiculo no puede ser mayor a la fecha actual"))
        }

        if (t.marca.isBlank()) {
            return Err(VehiculoError.VehiculoValidatorError("Marca invalida"))
        }

        if (t.tipo.isBlank()) {
            return Err(VehiculoError.VehiculoValidatorError("El tipo no puede estar en blanco"))
        }

        if (t.consumo.isBlank()) {
            return Err(VehiculoError.VehiculoValidatorError("El consumo no puede estar en blanco"))
        }

        return Ok(t)
    }
}