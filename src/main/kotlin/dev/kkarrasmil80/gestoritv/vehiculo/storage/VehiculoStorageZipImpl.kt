package dev.kkarrasmil80.gestoritv.vehiculo.storage

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import dev.kkarrasmil80.gestoritv.config.Config
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import com.github.michaelbull.result.Result
import dev.kkarrasmil80.gestoritv.vehiculo.error.VehiculoError
import dev.kkarrasmil80.gestoritv.vehiculo.models.Vehiculo
import org.lighthousegames.logging.logging
import java.util.zip.ZipEntry
import java.util.zip.ZipFile
import java.util.zip.ZipOutputStream

private val logger = logging()

class VehiculoStorageZipImpl(
    private val config: Config,
    private val storageCsv: VehiculoStorageCsv
) : VehiculoStorageZip {

    private val tempDirName = "vehiculos"

    /**
     * Exporta una lista de vehículos a un archivo ZIP.
     * al directorio temporal antes de comprimir todo.
     */
    override fun exportToZip(fileToZip: File, data: List<Vehiculo>): Result<File, VehiculoError> {
        logger.debug { "Exportando a ZIP $fileToZip" }
        val tempDir = Files.createTempDirectory(tempDirName)
        return try {
            // Copiar imágenes desde el directorio configurado al directorio temporal
            val imagesDir = File(config.imagesDirectory)
            if (imagesDir.exists() && imagesDir.isDirectory) {
                imagesDir.listFiles()?.forEach { file ->
                    Files.copy(
                        file.toPath(),
                        Paths.get(tempDir.toString(), file.name),
                        StandardCopyOption.REPLACE_EXISTING
                    )
                }
            }

            // Guardar los datos CSV/JSON en el directorio temporal
            storageCsv.writeToFile(File(tempDir.toFile(), "data.json"), data)

            Files.walk(tempDir).forEach { logger.debug { it } }

            // Crear archivo ZIP con los archivos del directorio temporal
            val archivos = Files.walk(tempDir)
                .filter { Files.isRegularFile(it) }
                .toList()
            ZipOutputStream(Files.newOutputStream(fileToZip.toPath())).use { zip ->
                archivos.forEach { archivo ->
                    val entradaZip = ZipEntry(tempDir.relativize(archivo).toString())
                    zip.putNextEntry(entradaZip)
                    Files.copy(archivo, zip)
                    zip.closeEntry()
                }
            }
            // Limpiar directorio temporal
            tempDir.toFile().deleteRecursively()

            Ok(fileToZip)
        } catch (e: Exception) {
            logger.error { "Error al exportar a ZIP: ${e.message}" }
            Err(VehiculoError.VehiculoStorageError("Error al exportar a ZIP: ${e.message}"))
        }
    }

    /**
     * Importa vehículos desde un archivo ZIP.
     * Extrae los archivos en un directorio temporal, copia las imágenes a la carpeta configurada
     * y lee los datos JSON para reconstruir la lista de vehículos.
     */
    override fun loadFromZip(fileToUnzip: File): Result<List<Vehiculo>, VehiculoError> {
        logger.debug { "Importando desde ZIP $fileToUnzip" }
        val tempDir = Files.createTempDirectory(tempDirName)
        return try {
            ZipFile(fileToUnzip).use { zip ->
                zip.entries().asSequence().forEach { entrada ->
                    zip.getInputStream(entrada).use { input ->
                        Files.copy(
                            input,
                            Paths.get(tempDir.toString(), entrada.name),
                            StandardCopyOption.REPLACE_EXISTING
                        )
                    }
                }
            }

            // Copiar archivos que no sean JSON (imágenes) al directorio configurado
            Files.walk(tempDir).forEach {
                if (!it.toString().endsWith(".json") && Files.isRegularFile(it)) {
                    val destino = Paths.get(config.imagesDirectory, it.fileName.toString())
                    Files.copy(
                        it,
                        destino,
                        StandardCopyOption.REPLACE_EXISTING
                    )
                }
            }

            // Leer datos JSON con los vehículos
            val data = storageCsv.readFromFile(File(tempDir.toFile(), "data.json"))

            // Eliminar directorio temporal
            tempDir.toFile().deleteRecursively()

            data
        } catch (e: Exception) {
            logger.error { "Error al importar desde ZIP: ${e.message}" }
            Err(VehiculoError.VehiculoStorageError("Error al importar desde ZIP: ${e.message}"))
        }
    }
}
