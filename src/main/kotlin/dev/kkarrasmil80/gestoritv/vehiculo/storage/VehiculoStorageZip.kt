package dev.kkarrasmil80.gestoritv.vehiculo.storage

import com.github.michaelbull.result.Result
import dev.kkarrasmil80.gestoritv.vehiculo.error.VehiculoError
import dev.kkarrasmil80.gestoritv.vehiculo.models.Vehiculo
import java.io.File

/**
 * Interfaz para almacenamiento de vehículos en formato comprimido ZIP.
 * Define métodos para exportar una lista de vehículos a un archivo ZIP
 * y para cargar vehículos desde un archivo ZIP.
 */
interface VehiculoStorageZip {

    /**
     * Exporta una lista de vehículos a un archivo ZIP.
     *
     * @param fileToZip Archivo ZIP donde se guardará la información.
     * @param data Lista de vehículos a exportar.
     * @return Resultado con el archivo ZIP generado o un error en caso de fallo.
     */
    fun exportToZip(fileToZip: File, data: List<Vehiculo>): Result<File, VehiculoError>

    /**
     * Carga una lista de vehículos desde un archivo ZIP.
     *
     * @param fileToUnzip Archivo ZIP desde el cual se leerán los vehículos.
     * @return Resultado con la lista de vehículos o un error en caso de fallo.
     */
    fun loadFromZip(fileToUnzip: File): Result<List<Vehiculo>, VehiculoError>
}
