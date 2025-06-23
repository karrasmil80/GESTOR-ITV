package dev.kkarrasmil80.gestoritv.vehiculo.storage

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Result
import dev.kkarrasmil80.gestoritv.vehiculo.error.VehiculoError
import dev.kkarrasmil80.gestoritv.vehiculo.models.Vehiculo
import java.io.File

/**
 * Implementación de almacenamiento de vehículos que delega
 * la lectura y escritura en el formato adecuado (CSV o JSON)
 * según la extensión del archivo proporcionado.
 */
class VehiculoStorageImpl(
    private val csv: VehiculoStorageCsv = VehiculoStorageCsv(),
    private val json: VehiculoStorageJson = VehiculoStorageJson()
) : VehiculoStorage {

    /**
     * Lee vehículos desde un archivo, seleccionando la implementación adecuada
     * según la extensión del archivo (.csv o .json)
     *
     * @param file Archivo del que se leerán los vehículos
     * @return Resultado con la lista de vehículos o error si el formato no es soportado o falla la lectura
     */
    override fun readFromFile(file: File): Result<List<Vehiculo>, VehiculoError> {
        val extension = file.extension.lowercase()
        return when (extension) {
            "csv" -> csv.readFromFile(file)
            "json" -> json.readFromFile(file)
            else -> Err(VehiculoError.VehiculoStorageError("Formato de archivo no soportado: .$extension"))
        }
    }

    /**
     * Escribe una lista de vehículos en un archivo, seleccionando la implementación adecuada
     * según la extensión del archivo (.csv o .json)
     *
     * @param file Archivo donde se escribirán los vehículos
     * @param vehiculo Lista de vehículos a almacenar
     * @return Resultado con mensaje de éxito o error si el formato no es soportado o falla la escritura
     */
    override fun writeToFile(file: File, vehiculo: List<Vehiculo>): Result<String, VehiculoError> {
        val extension = file.extension.lowercase()
        return when (extension) {
            "csv" -> csv.writeToFile(file, vehiculo)
            "json" -> json.writeToFile(file, vehiculo)
            else -> Err(VehiculoError.VehiculoStorageError("Formato de archivo no soportado: .$extension"))
        }
    }
}
