package dev.kkarrasmil80.gestoritv.vehiculo.storage

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import dev.kkarrasmil80.gestoritv.vehiculo.error.VehiculoError
import dev.kkarrasmil80.gestoritv.vehiculo.models.Vehiculo
import dev.kkarrasmil80.gestoritv.vehiculo.models.VehiculoElectrico
import dev.kkarrasmil80.gestoritv.vehiculo.models.VehiculoMotor
import dev.kkarrasmil80.gestoritv.vehiculo.models.VehiculoPublico
import org.lighthousegames.logging.logging
import java.io.File

private val logger = logging()

class VehiculoStorageCsv : VehiculoStorage {

    // Lee vehículos desde un archivo CSV
    override fun readFromFile(file: File): Result<List<Vehiculo>, VehiculoError> {
        // Validar que el archivo existe, es un fichero y se puede leer
        if (!file.exists() || !file.isFile || !file.canRead()) {
            return Err(VehiculoError.VehiculoStorageError("El fichero no existe, no es un fichero válido o no se puede leer."))
        }

        return try {
            Ok(
                file.readLines()
                    .drop(1) // Omitir la cabecera
                    .map { line ->
                        val campos = line.split(",").map { it.trim() }

                        // Validar cantidad de campos por línea
                        if (campos.size != 13) {
                            throw IllegalArgumentException("Línea del CSV mal formada: se esperaban 13 campos, se encontraron ${campos.size}. Campos: $campos")
                        }

                        val id = campos[0].toInt()
                        val matricula = campos[1]
                        val marca = campos[2]
                        val modelo = campos[3]
                        val anio = campos[4].toInt()
                        val tipo = campos[5].lowercase()

                        val neumaticos = campos[9].lowercase() == "true"
                        val bateria = campos[10].lowercase() == "true"
                        val frenos = campos[11].lowercase() == "true"

                        // Crear instancia según tipo de vehículo
                        when (tipo) {
                            "electrico" -> VehiculoElectrico(
                                id = id,
                                matricula = matricula,
                                marca = marca,
                                modelo = modelo,
                                anio = anio,
                                tipo = tipo,
                                consumo = campos[6],
                                neumaticos = neumaticos,
                                bateria = bateria,
                                frenos = frenos
                            )
                            "motor" -> VehiculoMotor(
                                id = id,
                                matricula = matricula,
                                marca = marca,
                                modelo = modelo,
                                anio = anio,
                                tipo = tipo,
                                cilindrada = campos[7].toIntOrNull() ?: 0,
                                neumaticos = neumaticos,
                                bateria = bateria,
                                frenos = frenos,
                                aceite = campos[12].toIntOrNull() ?: 0
                            )
                            "publico" -> VehiculoPublico(
                                id = id,
                                matricula = matricula,
                                marca = marca,
                                modelo = modelo,
                                anio = anio,
                                tipo = tipo,
                                capacidad = campos[8].toIntOrNull() ?: 0,
                                neumaticos = neumaticos,
                                bateria = bateria,
                                frenos = frenos
                            )
                            else -> throw IllegalArgumentException("Tipo de vehículo desconocido: $tipo")
                        }
                    }
            )
        } catch (e: Exception) {
            // Captura errores y devuelve error específico
            Err(VehiculoError.VehiculoStorageError("Error al procesar el CSV: ${e.message}"))
        }
    }

    // Guarda una lista de vehículos en archivo CSV
    override fun writeToFile(file: File, vehiculos: List<Vehiculo>): Result<String, VehiculoError> {
        logger.debug { "Escribiendo vehículos en el fichero CSV: $file" }

        val parentFile = file.parentFile
        if (parentFile != null) {
            // Crear directorio si no existe
            if (!parentFile.exists()) {
                parentFile.mkdirs()
            }
            // Validar que el padre es un directorio
            if (!parentFile.isDirectory) {
                logger.error { "El directorio padre no es un directorio: ${parentFile.absolutePath}" }
                return Err(VehiculoError.VehiculoStorageError("El directorio padre no es válido"))
            }
        }

        // Validar extensión .csv
        if (!file.name.endsWith(".csv", ignoreCase = true)) {
            logger.error { "El archivo no tiene extensión .csv: ${file.name}" }
            return Err(VehiculoError.VehiculoStorageError("El archivo debe tener extensión .csv"))
        }

        // Escribir cabecera CSV
        file.writeText("id,matricula,marca,modelo,anio,tipo,consumo,cilindrada,capacidad,neumaticos,bateria,frenos,aceite\n")

        // Escribir cada vehículo según su tipo
        vehiculos.forEach { vehiculo ->
            val csvRow = when (vehiculo) {
                is VehiculoElectrico -> listOf(
                    vehiculo.id.toString(),
                    vehiculo.matricula,
                    vehiculo.marca,
                    vehiculo.modelo,
                    vehiculo.anio.toString(),
                    vehiculo.tipo,
                    vehiculo.consumo,
                    "", // cilindrada vacía
                    "", // capacidad vacía
                    vehiculo.neumaticos.toString(),
                    vehiculo.bateria.toString(),
                    vehiculo.frenos.toString(),
                    ""  // aceite vacía
                ).joinToString(",")
                is VehiculoMotor -> listOf(
                    vehiculo.id.toString(),
                    vehiculo.matricula,
                    vehiculo.marca,
                    vehiculo.modelo,
                    vehiculo.anio.toString(),
                    vehiculo.tipo,
                    "", // consumo vacía
                    vehiculo.cilindrada.toString(),
                    "", // capacidad vacía
                    vehiculo.neumaticos.toString(),
                    vehiculo.bateria.toString(),
                    vehiculo.frenos.toString(),
                    vehiculo.aceite.toString()
                ).joinToString(",")
                is VehiculoPublico -> listOf(
                    vehiculo.id.toString(),
                    vehiculo.matricula,
                    vehiculo.marca,
                    vehiculo.modelo,
                    vehiculo.anio.toString(),
                    vehiculo.tipo,
                    "", // consumo vacía
                    "", // cilindrada vacía
                    vehiculo.capacidad.toString(),
                    vehiculo.neumaticos.toString(),
                    vehiculo.bateria.toString(),
                    vehiculo.frenos.toString(),
                    ""  // aceite vacía
                ).joinToString(",")
                else -> {
                    logger.error { "Tipo de vehículo desconocido: ${vehiculo::class.simpleName}" }
                    return Err(VehiculoError.VehiculoStorageError("Tipo de vehículo desconocido: ${vehiculo::class.simpleName}"))
                }
            }
            // Añadir línea al archivo
            file.appendText("$csvRow\n")
        }

        logger.info { "Vehículos guardados correctamente en $file" }
        return Ok(file.absolutePath)
    }
}
