package dev.kkarrasmil80.gestoritv.vehiculo.validator

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import dev.kkarrasmil80.gestoritv.vehiculo.error.VehiculoError
import dev.kkarrasmil80.gestoritv.vehiculo.models.Vehiculo

// Validador general para Vehiculo que verifica campos básicos y obligatorios
class VehiculoValidator : Validator<Vehiculo, VehiculoError> {
    override fun validate(t: Vehiculo): Result<Vehiculo, VehiculoError> {
        if (t.id <= 0)
            return Err(VehiculoError.VehiculoValidatorError("ID inválido"))

        if (t.matricula.isBlank())
            return Err(VehiculoError.VehiculoValidatorError("Matrícula inválida"))

        if (!t.matricula.matches(Regex("[0-9]{4}-[A-Z]{3}")))
            return Err(VehiculoError.VehiculoValidatorError("La matrícula debe seguir el patrón especificado"))

        if (t.modelo.isBlank())
            return Err(VehiculoError.VehiculoValidatorError("Modelo inválido"))

        if (t.anio > 2025)
            return Err(VehiculoError.VehiculoValidatorError("El vehículo no puede ser mayor a la fecha actual"))

        if (t.marca.isBlank())
            return Err(VehiculoError.VehiculoValidatorError("Marca inválida"))

        if (t.tipo.isBlank())
            return Err(VehiculoError.VehiculoValidatorError("El tipo no puede estar en blanco"))

        return Ok(t)
    }
}
