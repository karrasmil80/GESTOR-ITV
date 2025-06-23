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

    // Obtiene todos los vehículos
    override fun findAll(): Result<List<Vehiculo>, VehiculoError> {
        logger.debug { "Obteniendo lista de vehículos" }
        return Ok(repository.findAll())
    }

    // Busca vehículo por ID y usa cache
    override fun findById(id: Int): Result<Vehiculo, VehiculoError> {
        logger.debug { "Obteniendo vehículo por ID: $id" }
        val vehiculo = repository.findById(id)
        return if (vehiculo != null) {
            cache.getIfPresent(id)
            Ok(vehiculo)
        } else {
            logger.error { "Vehículo no encontrado con ID: $id" }
            Err(VehiculoError.VehiculoIdNotFound("No se encontró vehículo con ID $id"))
        }
    }

    // Guarda vehículo y actualiza cache
    override fun save(vehiculo: Vehiculo): Result<Int, VehiculoError> {
        val saved = repository.save(vehiculo)
        cache.put(vehiculo.id, vehiculo)
        return Ok(saved)
    }

    // Elimina vehículo por ID y limpia cache
    override fun deleteById(id: Int): Result<Int, VehiculoError> {
        val deleted = repository.deleteById(id)
        return if (deleted > 0) {
            cache.invalidate(id)
            Ok(deleted)
        } else {
            Err(VehiculoError.VehiculoIdNotFound("No se pudo eliminar vehículo con ID $id"))
        }
    }

    // Elimina todos los vehículos y limpia cache completa
    override fun deleteAll(): Result<Int, VehiculoError> {
        val deletedAll = repository.deleteAll()
        return if (deletedAll > 0) {
            cache.invalidateAll()
            Ok(deletedAll)
        } else {
            Err(VehiculoError.VehiculoIdNotFound("No se pudieron eliminar vehículos"))
        }
    }
}
