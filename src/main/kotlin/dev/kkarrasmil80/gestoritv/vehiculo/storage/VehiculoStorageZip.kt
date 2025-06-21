package dev.kkarrasmil80.gestoritv.vehiculo.storage

import com.github.michaelbull.result.Result
import dev.kkarrasmil80.gestoritv.vehiculo.error.VehiculoError
import dev.kkarrasmil80.gestoritv.vehiculo.models.Vehiculo
import java.io.File

interface VehiculoStorageZip {
    fun exportToZip(fileToZip: File, data: List<Vehiculo>): Result<File, VehiculoError>
    fun loadFromZip(fileToUnzip: File): Result<List<Vehiculo>, VehiculoError>
}