package dev.kkarrasmil80.gestoritv.vehiculo.service

import com.github.michaelbull.result.Result
import dev.kkarrasmil80.gestoritv.vehiculo.error.VehiculoError
import dev.kkarrasmil80.gestoritv.vehiculo.models.Vehiculo

interface VehiculoService {
    fun findAll(): Result<List<Vehiculo>, VehiculoError>
    fun findById(id: Int): Result<Vehiculo, VehiculoError>
    fun save(vehiculo: Vehiculo): Result<Int, VehiculoError>
    fun deleteById(id: Int): Result<Int, VehiculoError>
    fun deleteAll(): Result<Int, VehiculoError>
}