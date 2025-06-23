package dev.kkarrasmil80.gestoritv.vehiculo.repositories

import dev.kkarrasmil80.gestoritv.vehiculo.dao.VehiculoDao
import dev.kkarrasmil80.gestoritv.vehiculo.mapper.toEntity
import dev.kkarrasmil80.gestoritv.vehiculo.mapper.toModel
import dev.kkarrasmil80.gestoritv.vehiculo.models.Vehiculo
import org.lighthousegames.logging.logging

private val logger = logging()

class VehiculoRepositoryImpl(
    private val dao: VehiculoDao,
) : VehiculoRepository {

    // Obtener todos los vehículos
    override fun findAll(): List<Vehiculo> {
        logger.debug { "Obteniendo todos los vehiculos" }
        return dao.getAll().map { it.toModel() }
    }

    // Buscar vehículo por ID
    override fun findById(id: Int): Vehiculo? {
        logger.debug { "Buscando vehiculo por id $id" }
        return dao.getById(id)?.toModel()
    }

    // Guardar un vehículo (insertar)
    override fun save(vehiculo: Vehiculo): Int {
        logger.debug { "Guardando vehiculo ${vehiculo.id}" }
        return dao.insert(vehiculo.toEntity())
    }

    // Eliminar vehículo por ID
    override fun deleteById(id: Int): Int {
        logger.debug { "Borrando vehiculo $id" }
        return dao.deleteById(id)
    }

    // Eliminar todos los vehículos
    override fun deleteAll(): Int {
        logger.debug { "Borrando todos los vehiculos" }
        return dao.deleteAll()
    }
}
