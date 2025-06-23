package dev.kkarrasmil80.gestoritv.vehiculo.storage

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import dev.kkarrasmil80.gestoritv.vehiculo.dto.VehiculoDto
import dev.kkarrasmil80.gestoritv.vehiculo.error.VehiculoError
import dev.kkarrasmil80.gestoritv.vehiculo.models.Vehiculo
import kotlinx.serialization.json.Json
import com.github.michaelbull.result.Result
import dev.kkarrasmil80.gestoritv.vehiculo.mapper.toDto
import dev.kkarrasmil80.gestoritv.vehiculo.mapper.toModel
import kotlinx.serialization.encodeToString
import java.io.File

class VehiculoStorageJson : VehiculoStorage {

    // Configuración del objeto Json para formateo legible y para ignorar claves desconocidas en JSON
    private val json = Json { prettyPrint = true; ignoreUnknownKeys = true }

    // Método para leer vehículos desde un archivo JSON
    override fun readFromFile(file: File): Result<List<Vehiculo>, VehiculoError> {
        // Validar que el archivo exista, sea un archivo y se pueda leer
        if (!file.exists() || !file.isFile || !file.canRead()) {
            return Err(VehiculoError.VehiculoStorageError("No se puede leer el fichero: ${file.path}"))
        }

        return try {
            // Leer todo el contenido del archivo como texto
            val content = file.readText()

            // Decodificar el contenido JSON a una lista de DTOs de vehículos
            val dto = json.decodeFromString<List<VehiculoDto>>(content)

            Ok(dto.map { it.toModel() })
        } catch (e: Exception) {

            Err(VehiculoError.VehiculoStorageError("Error al leer JSON: ${e.message}"))
        }
    }

    // Método para escribir una lista de vehículos a un archivo JSON
    override fun writeToFile(file: File, vehiculos: List<Vehiculo>): Result<String, VehiculoError> {
        return try {
            // Convertir los modelos Vehiculo a DTOs
            val dto = vehiculos.map { it.toDto() }

            file.writeText(json.encodeToString(dto))

            Ok("Vehículos guardados correctamente en ${file.path}")
        } catch (e: Exception) {

            Err(VehiculoError.VehiculoStorageError("Error al escribir JSON: ${e.message}"))
        }
    }
}
