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

        return Ok(file.readLines()
            .drop(1)
            .map { it.split(",") }
            .map { it -> it.map { it.trim() } }
            .map {
                val id =  it[0].toInt()
                val matricula = it[1]
                val marca = it[2]
                val modelo = it[3]
                val anio = it[4].toInt()
                val tipo = it[5]

                when(tipo.lowercase()) {
                    "electrico" -> {
                        val consumo = it[6]
                        VehiculoElectrico(
                            id = id,
                            matricula = matricula,
                            marca = marca,
                            modelo = modelo,
                            anio = anio,
                            tipo = tipo,
                            consumo = consumo
                        )
                    }

                    "motor" -> {
                        val cilindrada = it[7].toInt()
                        VehiculoMotor(
                            id = id,
                            matricula = matricula,
                            marca = marca,
                            modelo = modelo,
                            anio = anio,
                            tipo = tipo,
                            cilindrada = cilindrada,
                        )
                    }

                    "publico"  ->  {
                        val capacidad = it[8].toInt()
                        VehiculoPublico(
                            id = id,
                            matricula = matricula,
                            marca = marca,
                            modelo = modelo,
                            anio = anio,
                            tipo = tipo,
                            capacidad = capacidad,
                        )
                    }

                    else -> throw IllegalArgumentException("Tipo no reconocido")
                }
            })
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
        file.writeText("id,matricula,marca,modelo,anio,tipo,consumo,cilindrada,capacidad\n")

        vehiculos.forEach { vehiculo ->
            val csvRow = when (vehiculo) {
                is VehiculoElectrico -> {
                    "${vehiculo.id},${vehiculo.matricula},${vehiculo.marca},${vehiculo.modelo},${vehiculo.anio},${vehiculo.tipo},${vehiculo.consumo},,"
                }
                is VehiculoMotor -> {
                    "${vehiculo.id},${vehiculo.matricula},${vehiculo.marca},${vehiculo.modelo},${vehiculo.anio},${vehiculo.tipo},,${vehiculo.cilindrada},"
                }
                is VehiculoPublico -> {
                    "${vehiculo.id},${vehiculo.matricula},${vehiculo.marca},${vehiculo.modelo},${vehiculo.anio},${vehiculo.tipo},,,${vehiculo.capacidad}"
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