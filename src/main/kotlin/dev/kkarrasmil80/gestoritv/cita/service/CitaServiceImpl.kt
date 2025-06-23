package dev.kkarrasmil80.gestoritv.cita.service

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import dev.kkarrasmil80.gestoritv.cita.error.CitaError
import dev.kkarrasmil80.gestoritv.cita.mapper.toEntity
import dev.kkarrasmil80.gestoritv.cita.models.Cita
import dev.kkarrasmil80.gestoritv.cita.repositories.CitasRepositoryImpl
import dev.kkarrasmil80.gestoritv.cita.validator.CitaValidator
import dev.kkarrasmil80.gestoritv.vehiculo.repositories.VehiculoRepositoryImpl
import dev.kkarrasmil80.gestoritv.vehiculo.validator.Validator

class CitaServiceImpl (
    private val repository: CitasRepositoryImpl,
    private val repositoryVehiculo : VehiculoRepositoryImpl,
    private val validador: CitaValidator
) : CitaService {

    // Obtiene todas las citas sin filtro
    override fun getAll(): Result<List<Cita>, CitaError> {
        return Ok(repository.getAll())
    }

    // Obtiene una cita por id, o error si no existe
    override fun getById(id: Int): Result<Cita, CitaError> {
        return repository.getById(id)?.let {
            Ok(it)
        } ?: Err(CitaError.CitaIdNotFound("No se ha podido encontrar la cita por que no existe el id"))
    }

    // Inserta una nueva cita tras validarla, verificar vehículo y evitar duplicados en fecha y hora
    override fun insert(cita: Cita): Result<Int, CitaError> {
        // Validar cita
        val validador = validador.validate(cita)
        if (validador.isErr) {
            return Err(CitaError.CitaValidatorError("Error al validar la cita, no está bien formada o faltan datos"))
        }

        val vehiculo = cita.vehiculo ?: return Err(CitaError.CitaServiceError("El vehículo no puede ser nulo"))

        val resultadoVehiculo = repositoryVehiculo.save(vehiculo)
        if (resultadoVehiculo == null) {
            return Err(CitaError.CitaServiceError("El vehículo no existe en la base de datos"))
        }

        if (repository.fechaYHoraCita(cita.hora, cita.fechaCita)) {
            return Err(CitaError.CitaServiceError("Ya existe una cita a esa hora"))
        }

        val id = repository.insert(cita)
        return Ok(id)
    }

    // Elimina una cita por id, devuelve error si no se encuentra
    override fun deleteById(id: Int): Result<Int, CitaError> {
        val filasAfectadas = repository.deleteById(id)
        if (filasAfectadas > 0) {
            return Ok(filasAfectadas)
        } else {
            return Err(CitaError.CitaServiceError("No se ha podido encontrar la cita"))
        }
    }

    // Elimina todas las citas y devuelve la cantidad eliminada
    override fun deleteAll(): Result<Int, CitaError> {
        return Ok(repository.deleteAll())
    }
}
