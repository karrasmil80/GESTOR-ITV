package dev.kkarrasmil80.gestoritv.vehiculo.validator

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import dev.kkarrasmil80.gestoritv.vehiculo.error.VehiculoError
import dev.kkarrasmil80.gestoritv.vehiculo.models.VehiculoMotor

// Validador para VehiculoMotor que verifica sus propiedades básicas y específicas
class VehiculoMotorValidator : Validator<VehiculoMotor, VehiculoError> {

    override fun validate(t: VehiculoMotor): Result<VehiculoMotor, VehiculoError> {
        // Valida que el id sea mayor que 0
        if (t.id <= 0)
            return Err(VehiculoError.VehiculoValidatorError("ID inválido"))

        // Valida que la matrícula no esté vacía
        if (t.matricula.isBlank())
            return Err(VehiculoError.VehiculoValidatorError("Matrícula inválida"))

        // Valida que la matrícula cumpla el patrón 0000-AAA
        if (!t.matricula.matches(Regex("[0-9]{4}-[A-Z]{3}")))
            return Err(VehiculoError.VehiculoValidatorError("La matrícula debe seguir el patrón especificado"))

        // Modelo no puede estar vacío
        if (t.modelo.isBlank())
            return Err(VehiculoError.VehiculoValidatorError("Modelo inválido"))

        // Año no puede ser mayor al 2025
        if (t.anio > 2025)
            return Err(VehiculoError.VehiculoValidatorError("El vehículo no puede ser mayor a la fecha actual"))

        // Marca no puede estar vacía
        if (t.marca.isBlank())
            return Err(VehiculoError.VehiculoValidatorError("Marca inválida"))

        // Tipo no puede estar vacío
        if (t.tipo.isBlank())
            return Err(VehiculoError.VehiculoValidatorError("El tipo no puede estar en blanco"))

        // La cilindrada no puede ser 0
        if (t.cilindrada == 0)
            return Err(VehiculoError.VehiculoValidatorError("La cilindrada no puede ser 0"))

        // Los frenos deben pasar la prueba
        if (!t.frenos)
            return Err(VehiculoError.VehiculoValidatorError("Los frenos no han pasado la prueba"))

        // La batería debe pasar la prueba
        if (!t.bateria)
            return Err(VehiculoError.VehiculoValidatorError("La batería no ha pasado la prueba"))

        // Los neumáticos deben pasar la prueba
        if (!t.neumaticos)
            return Err(VehiculoError.VehiculoValidatorError("Los neumáticos no han pasado la prueba"))

        // El nivel de aceite debe ser mayor a 3
        if (t.aceite <= 3)
            return Err(VehiculoError.VehiculoValidatorError("El aceite es menor del indicado, no han pasado la prueba"))

        // Si todo es válido, retorna Ok con el vehículo
        return Ok(t)
    }
}
