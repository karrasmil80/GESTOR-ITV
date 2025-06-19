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
    override fun getAll(): Result<List<Cita>, CitaError> {
       return Ok(repository.getAll())
    }


    override fun getById(id: Int): Result<Cita, CitaError> {
        return repository.getById(id)?.let {
            Ok(it)
        } ?: Err(CitaError.CitaIdNotFound("No se ha podido encontrar la cita por que no existe el id"))
    }


    override fun insert(cita: Cita): Result<Cita, CitaError> {
        val validador = validador.validate(cita)
        if (validador.isErr) {
            return Err(CitaError.CitaValidatorError("Error al validar la cita, no esta bien formada o faltan datos"))
        }
        val vehiculo = repositoryVehiculo.save(cita.vehiculo!!)?.let {
            Ok(it)
        } ?: Err(CitaError.CitaServiceError("El vehiculo no existe en la base de datos"))

        if (repository.fechaYHoraCita(cita.hora, cita.fechaCita)) {
            return Err(CitaError.CitaServiceError("Ya existe una cita a esa hora"))
        }

        val id = repository.insert(cita)
        return Ok(cita.copy(id = id))
    }

    override fun deleteById(id: Int): Result<Unit, CitaError> {
        val filasAfectadas = repository.deleteById(id)
        if (filasAfectadas > 0) {
            return Ok(Unit)
        } else {
            return Err(CitaError.CitaServiceError("No se ha podido encontrar la cita"))
        }
    }

    override fun deleteAll(): Result<Int, CitaError> {
        return Ok(repository.deleteAll())
    }
}