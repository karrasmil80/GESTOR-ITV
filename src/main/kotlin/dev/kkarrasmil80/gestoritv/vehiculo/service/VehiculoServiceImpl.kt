package dev.kkarrasmil80.gestoritv.vehiculo.service

import com.github.benmanes.caffeine.cache.Cache
import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import dev.kkarrasmil80.gestoritv.vehiculo.error.VehiculoError
import dev.kkarrasmil80.gestoritv.vehiculo.models.Vehiculo
import dev.kkarrasmil80.gestoritv.vehiculo.repositories.VehiculoRepositoryImpl
import org.lighthousegames.logging.logging

private val logger = logging()
class VehiculoServiceImpl(
    private val repository: VehiculoRepositoryImpl,
    private val cache: Cache<Int, Vehiculo>
) : VehiculoService {

    override fun findAll(): Result<List<Vehiculo>, VehiculoError> {
        logger.debug { "obteniendo lista de vehiculos" }
        val vehiculo = repository.findAll()
        return Ok(vehiculo)
    }

    override fun findById(id: Int): Result<Vehiculo, VehiculoError> {
        logger.debug { "Obteniendo vehiculo por identificador : $id" }
        val vehiculo = repository.findById(id)
        return if (vehiculo != null) {
            cache.getIfPresent(id)
            Ok(vehiculo)
        } else {
            logger.error { "No se ha encontrado el vehiculo con id : $id" }
            Err(VehiculoError.VehiculoIdNotFound("No se ha encontrado el vehiculo con id : $id"))
        }
    }

    override fun save(vehiculo: Vehiculo): Result<Int, VehiculoError> {
        val savedVehiculo = repository.save(vehiculo)
        cache.put(vehiculo.id, vehiculo)
        return Ok(savedVehiculo)
    }

    override fun deleteById(id: Int): Result<Int, VehiculoError> {
        val delete = repository.deleteById(id)
        return if (delete> 0) {
            cache.invalidate(id)
            Ok(delete)
        } else {
            Err(VehiculoError.VehiculoIdNotFound("No se pudo eliminar el vehículo con id $id"))
        }
    }


    override fun deleteAll(): Result<Int, VehiculoError> {
        val deleteAll = repository.deleteAll()
        return if (deleteAll > 0) {
            cache.invalidateAll()
            Ok(deleteAll)
        } else {
            Err(VehiculoError.VehiculoIdNotFound("No se pudieron eliminar vehículos"))
        }
    }


}