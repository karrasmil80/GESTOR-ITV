package vehiculo.storage

import com.github.michaelbull.result.getError
import dev.kkarrasmil80.gestoritv.vehiculo.error.VehiculoError
import dev.kkarrasmil80.gestoritv.vehiculo.models.VehiculoElectrico
import dev.kkarrasmil80.gestoritv.vehiculo.models.VehiculoMotor
import dev.kkarrasmil80.gestoritv.vehiculo.models.VehiculoPublico
import dev.kkarrasmil80.gestoritv.vehiculo.storage.VehiculoStorageCsv
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.io.File
class VehiculoStorageCsvTest {

    private val vehiculoElectrico = VehiculoElectrico(
        id = 100,
        matricula = "1234-ZZZ",
        marca = "Tesla",
        modelo = "Model S",
        anio = 2022,
        tipo = "electrico",
        consumo = "15kWh/100km",
        neumaticos = true,
        bateria = true,
        frenos = true,
    )

    private val vehiculoPublico = VehiculoPublico(
        id = 101,
        matricula = "1234-ZZC",
        marca = "Mercedes",
        modelo = "Citaro",
        anio = 2018,
        tipo = "publico",
        capacidad = 50,
        neumaticos = true,
        bateria = true,
        frenos = true
    )

    private val vehiculoMotor = VehiculoMotor(
        id = 100,
        matricula = "1234-ZZZ",
        marca = "Mercedes",
        modelo = "Model S",
        anio = 2022,
        tipo = "motor",
        cilindrada = 3,
        aceite = 5,
        neumaticos = true,
        bateria = true,
        frenos = true
    )

    private val tempFile = File("data_backup/vehiculos.csv")

    private val storageCsv = VehiculoStorageCsv()

    @Nested
    @DisplayName("Casos correctos")
    inner class CasosCorrectos {

        @Test
        @DisplayName("Deberia de leer un csv correctamente")
        fun readFile() {
            val leerFichero = storageCsv.readFromFile(tempFile)

            assertTrue(leerFichero.isOk)
            assertTrue(leerFichero.value.isNotEmpty())
            assertTrue(tempFile.exists())
        }

        @Test
        @DisplayName("Deberia de escribir en un csv correctamente")
        fun writeFile() {

            val escribirFichero = storageCsv.writeToFile(tempFile, listOf(vehiculoElectrico, vehiculoPublico, vehiculoMotor))

            assertTrue(escribirFichero.isOk)
            assertTrue(escribirFichero.value.isNotEmpty())
            assertTrue(tempFile.exists())
        }
    }

    @Nested
    @DisplayName("Casos Incorrectos")
    inner class CasosIncorrectos {
        @Test
        @DisplayName("El archivo no deberia de existir")
        fun archivoNoExiste() {

            val file = File("/data_backup/vehiculo_no_existe.csv")
            val leerFichero = storageCsv.readFromFile(file)
            assertTrue(leerFichero.isErr)


        }

        @Test
        @DisplayName("Deberia de leer un archivo inexistente.")
        fun testReadNonExistentFile() {
            val File = File("no_existo.csv")
            val result = storageCsv.readFromFile(File)

            assertTrue(result.getError() is VehiculoError.VehiculoStorageError)
        }

        @Test
        @DisplayName("Deberia fallar al intentar escribir en un archivo cuyo directorio padre no existe.")
        fun writeFileSinDirectorioPadre() {

            val drectorioInexistente = File("nonExistentDirBloqueada")

            if (drectorioInexistente.exists()) {
                drectorioInexistente.deleteRecursively()
            }

            drectorioInexistente.writeText("no existo")

            val tempFile = File(drectorioInexistente, "test.csv")
            val result = storageCsv.writeToFile(tempFile, listOf(vehiculoElectrico))

            assertTrue(result.isErr)
            assertTrue(result.error is VehiculoError.VehiculoStorageError)
            assertEquals("El directorio padre no es válido", result.error.message)

            drectorioInexistente.delete()
        }
    }
}
