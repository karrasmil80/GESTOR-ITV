package dev.kkarrasmil80.gestoritv.vehiculo.storage

import dev.kkarrasmil80.gestoritv.vehiculo.error.VehiculoError
import dev.kkarrasmil80.gestoritv.vehiculo.models.Vehiculo
import com.github.michaelbull.result.Result
import java.io.File

// Interfaz para almacenamiento de vehículos en archivos
interface VehiculoStorage {
    // Lee una lista de vehículos desde un archivo
    fun readFromFile(file: File): Result<List<Vehiculo>, VehiculoError>

    // Escribe una lista de vehículos en un archivo, retorna mensaje o error
    fun writeToFile(file: File, persona: List<Vehiculo>): Result<String, VehiculoError>
}
