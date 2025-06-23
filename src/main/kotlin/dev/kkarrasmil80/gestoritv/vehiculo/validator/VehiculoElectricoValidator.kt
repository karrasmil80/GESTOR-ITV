package dev.kkarrasmil80.gestoritv.vehiculo.validator

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import dev.kkarrasmil80.gestoritv.vehiculo.error.VehiculoError
import dev.kkarrasmil80.gestoritv.vehiculo.models.VehiculoElectrico

// Validador específico para VehiculoElectrico que implementa la interfaz Validator
class VehiculoElectricoValidator : Validator<VehiculoElectrico, VehiculoError> {

    // Valida los campos del vehículo eléctrico y retorna Ok si es válido o Err con error específico
    override fun validate(t: VehiculoElectrico): Result<VehiculoElectrico, VehiculoError> {

        if (t.id <= 0)
            return Err(VehiculoError.VehiculoValidatorError("ID inválido"))

        if (t.matricula.isBlank())
            return Err(VehiculoError.VehiculoValidatorError("Matrícula inválida"))

        // Valida que la matrícula siga el patrón 0000-AAA
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

        if (t.consumo.isBlank())
            return Err(VehiculoError.VehiculoValidatorError("El consumo no puede estar en blanco"))

        // Validación de estado de frenos, batería y neumáticos
        if (!t.frenos)
            return Err(VehiculoError.VehiculoValidatorError("Los frenos no han pasado la prueba"))

        if (!t.bateria)
            return Err(VehiculoError.VehiculoValidatorError("La batería no ha pasado la prueba"))

        if (!t.neumaticos)
            return Err(VehiculoError.VehiculoValidatorError("Los neumáticos no han pasado la prueba"))

        // Si todo es correcto, retorna Ok con el vehículo
        return Ok(t)
    }
}
