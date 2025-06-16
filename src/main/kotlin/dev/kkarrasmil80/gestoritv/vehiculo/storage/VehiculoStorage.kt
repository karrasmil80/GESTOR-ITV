package dev.kkarrasmil80.gestoritv.vehiculo.storage

import dev.kkarrasmil80.gestoritv.vehiculo.error.VehiculoError
import dev.kkarrasmil80.gestoritv.vehiculo.models.Vehiculo
import com.github.michaelbull.result.Result
import java.io.File

interface VehiculoStorage {
    fun readFromFile (file: File): Result<List<Vehiculo>, VehiculoError>
    fun writeToFile (file: File, persona: List<Vehiculo>): Result<String, VehiculoError>
}