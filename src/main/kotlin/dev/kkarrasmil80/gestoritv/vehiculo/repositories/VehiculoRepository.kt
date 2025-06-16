package dev.kkarrasmil80.gestoritv.vehiculo.repositories
import dev.kkarrasmil80.gestoritv.vehiculo.models.Vehiculo

interface VehiculoRepository {
    fun findAll(): List<Vehiculo>
    fun findById(id: Int): Vehiculo?
    fun save(vehiculo: Vehiculo): Int
    fun deleteById(id: Int): Int
    fun deleteAll(): Int
}