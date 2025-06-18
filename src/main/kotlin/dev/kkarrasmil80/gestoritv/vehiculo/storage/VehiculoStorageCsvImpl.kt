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

class VehiculoStorageCsvImpl (

) : VehiculoStorage {
    override fun readFromFile(file: File): Result<List<Vehiculo>, VehiculoError> {
        logger.debug { "Leyendo el archivo..." }

        if (!file.isFile || !file.exists() || !file.canRead()) {
            logger.error { "El fichero no existe o no se puede leer: $file" }
            return Err(VehiculoError.VehiculoStorageError("El fichero no existe o no se puede leer: $file"))
        }

        return try {
            val vehiculos = file.readLines()
                .drop(1)
                .map { it.split(",") }
                .map { it.map { campo -> campo.trim() } }
                .map { campos ->
                    val id = campos[0].toInt()
                    val matricula = campos[1]
                    val marca = campos[2]
                    val modelo = campos[3]
                    val anio = campos[4].toInt()
                    val tipo = campos[5].lowercase()

                    // Campos que pueden estar vacíos
                    val consumo = if (campos[6].isEmpty()) null else campos[6]
                    val cilindrada = if (campos[7].isEmpty()) null else campos[7].toInt()
                    val capacidad = if (campos[8].isEmpty()) null else campos[8].toInt()
                    val neumaticos = if (campos.size > 9 && campos[9].lowercase() == "true") true else false
                    val bateria = if (campos.size > 10 && campos[10].lowercase() == "true") true else false
                    val frenos = if (campos.size > 11 && campos[11].lowercase() == "true") true else false
                    val aceite = if (campos.size > 12 && campos[12].isNotEmpty()) campos[12].toInt() else null


                    when (tipo) {
                        "electrico" -> {
                            VehiculoElectrico(
                                id = id,
                                matricula = matricula,
                                marca = marca,
                                modelo = modelo,
                                anio = anio,
                                tipo = tipo,
                                consumo = consumo ?: throw IllegalArgumentException("Falta consumo para eléctrico"),
                                neumaticos = neumaticos,
                                bateria = bateria,
                                frenos = frenos
                            )
                        }

                        "motor" -> {
                            VehiculoMotor(
                                id = id,
                                matricula = matricula,
                                marca = marca,
                                modelo = modelo,
                                anio = anio,
                                tipo = tipo,
                                cilindrada = cilindrada
                                    ?: throw IllegalArgumentException("Falta cilindrada para motor"),
                                aceite = aceite!!,
                                neumaticos = neumaticos,
                                bateria = bateria,
                                frenos = frenos
                            )
                        }

                        "publico" -> {
                            VehiculoPublico(
                                id = id,
                                matricula = matricula,
                                marca = marca,
                                modelo = modelo,
                                anio = anio,
                                tipo = tipo,
                                capacidad = capacidad ?: throw IllegalArgumentException("Falta capacidad para público"),
                                neumaticos = neumaticos,
                                bateria = bateria,
                                frenos = frenos
                            )
                        }

                        else -> throw IllegalArgumentException("Tipo no reconocido: $tipo")
                    }
                }
            Ok(vehiculos)
        } catch (e: Exception) {
            logger.error { "Error leyendo CSV: ${e.message}" }
            Err(VehiculoError.VehiculoStorageError("Error leyendo CSV: ${e.message}"))
        }
    }

    override fun writeToFile(file: File, vehiculos: List<Vehiculo>): Result<String, VehiculoError> {
        logger.debug { "Escribiendo vehículos en el fichero CSV: $file" }

        val parentFile = file.parentFile
        if (parentFile != null) {
            if (!parentFile.exists()) {
                parentFile.mkdirs()
            }
            if (!parentFile.isDirectory) {
                logger.error { "El directorio padre no es un directorio: ${parentFile.absolutePath}" }
                return Err(VehiculoError.VehiculoStorageError("El directorio padre no es válido"))
            }
        }

        if (!file.name.endsWith(".csv", ignoreCase = true)) {
            logger.error { "El archivo no tiene extensión .csv: ${file.name}" }
            return Err(VehiculoError.VehiculoStorageError("El archivo debe tener extensión .csv"))
        }

        // Escribimos la cabecera
        file.writeText("id,matricula,marca,modelo,anio,tipo,consumo,cilindrada,capacidad,neumaticos,bateria,frenos,aceite\n")



        vehiculos.forEach { vehiculo ->
            val csvRow = when (vehiculo) {
                is VehiculoElectrico -> {
                    "${vehiculo.id},${vehiculo.matricula},${vehiculo.marca},${vehiculo.modelo},${vehiculo.anio},${vehiculo.tipo},${vehiculo.consumo},,,${vehiculo.neumaticos},${vehiculo.bateria},${vehiculo.frenos},"
                }
                is VehiculoMotor -> {
                    "${vehiculo.id},${vehiculo.matricula},${vehiculo.marca},${vehiculo.modelo},${vehiculo.anio},${vehiculo.tipo},,${vehiculo.cilindrada},,${vehiculo.neumaticos},${vehiculo.bateria},${vehiculo.frenos},${vehiculo.aceite}"
                }
                is VehiculoPublico -> {
                    "${vehiculo.id},${vehiculo.matricula},${vehiculo.marca},${vehiculo.modelo},${vehiculo.anio},${vehiculo.tipo},,,${vehiculo.capacidad},${vehiculo.neumaticos},${vehiculo.bateria},${vehiculo.frenos},"
                }
                else -> {
                    logger.error { "Tipo de vehículo desconocido: ${vehiculo::class.simpleName}" }
                    return Err(VehiculoError.VehiculoStorageError("Tipo de vehículo desconocido: ${vehiculo::class.simpleName}"))
                }
            }
            file.appendText("$csvRow\n")
        }

        logger.info { "Vehículos guardados correctamente en $file" }
        return Ok(file.absolutePath)
    }
}