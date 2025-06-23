package dev.kkarrasmil80.gestoritv.vehiculo.service

import com.github.michaelbull.result.Result
import dev.kkarrasmil80.gestoritv.vehiculo.error.VehiculoError
import dev.kkarrasmil80.gestoritv.vehiculo.models.Vehiculo

interface VehiculoService {

    /**
     * Obtiene la lista de todos los vehículos.
     * @return Result que contiene una lista de Vehiculo o un VehiculoError en caso de fallo.
     */
    fun findAll(): Result<List<Vehiculo>, VehiculoError>

    /**
     * Busca un vehículo por su ID.
     * @param id Identificador del vehículo a buscar.
     * @return Result que contiene el Vehiculo encontrado o un VehiculoError en caso de fallo o no encontrado.
     */
    fun findById(id: Int): Result<Vehiculo, VehiculoError>

    /**
     * Guarda un vehículo en el sistema.
     * @param vehiculo Vehiculo a guardar.
     * @return Result que contiene el ID del vehículo guardado o un VehiculoError en caso de fallo.
     */
    fun save(vehiculo: Vehiculo): Result<Int, VehiculoError>

    /**
     * Elimina un vehículo por su ID.
     * @param id Identificador del vehículo a eliminar.
     * @return Result que contiene la cantidad de registros eliminados o un VehiculoError en caso de fallo.
     */
    fun deleteById(id: Int): Result<Int, VehiculoError>

    /**
     * Elimina todos los vehículos.
     * @return Result que contiene la cantidad de registros eliminados o un VehiculoError en caso de fallo.
     */
    fun deleteAll(): Result<Int, VehiculoError>
}
