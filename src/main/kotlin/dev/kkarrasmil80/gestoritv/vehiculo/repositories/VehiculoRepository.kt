package dev.kkarrasmil80.gestoritv.vehiculo.repositories

import dev.kkarrasmil80.gestoritv.vehiculo.models.Vehiculo

/**
 * Repositorio para operaciones CRUD sobre Vehículos.
 */
interface VehiculoRepository {

    /** Devuelve la lista de todos los vehículos. */
    fun findAll(): List<Vehiculo>

    /** Busca un vehículo por su ID, o null si no existe. */
    fun findById(id: Int): Vehiculo?

    /** Guarda un vehículo y devuelve su ID generado. */
    fun save(vehiculo: Vehiculo): Int

    /** Elimina un vehículo por ID y devuelve la cantidad de registros eliminados. */
    fun deleteById(id: Int): Int

    /** Elimina todos los vehículos y devuelve la cantidad de registros eliminados. */
    fun deleteAll(): Int
}
