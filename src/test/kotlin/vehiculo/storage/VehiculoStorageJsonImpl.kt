package vehiculo.storage


import com.github.michaelbull.result.getError
import dev.kkarrasmil80.gestoritv.vehiculo.error.VehiculoError
import dev.kkarrasmil80.gestoritv.vehiculo.models.VehiculoElectrico
import dev.kkarrasmil80.gestoritv.vehiculo.models.VehiculoMotor
import dev.kkarrasmil80.gestoritv.vehiculo.models.VehiculoPublico
import dev.kkarrasmil80.gestoritv.vehiculo.storage.VehiculoStorageJson
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import java.io.File
import kotlin.io.path.Path
import kotlin.io.path.deleteIfExists
import kotlin.io.path.exists

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class VehiculoStorageJsonTestImpl {

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
        id = 102,
        matricula = "1234-ZZA",
        marca = "Renault",
        modelo = "Clio",
        anio = 2019,
        tipo = "motor",
        cilindrada = 1,
        aceite = 3,
        neumaticos = true,
        bateria = true,
        frenos = true
    )

    private val tempFile = File("data_test/vehiculos.json")
    private val storageJson = VehiculoStorageJson()

    @Nested
    @DisplayName("Casos correctos")
    inner class CasosCorrectos {

        @Test
        @DisplayName("Debería escribir en un archivo JSON correctamente")
        fun writeFile() {
            val result = storageJson.writeToFile(tempFile, listOf(vehiculoElectrico, vehiculoMotor, vehiculoPublico))

            assertTrue(result.isOk)
            assertTrue(result.value.contains("Vehículos guardados correctamente"))
            assertTrue(tempFile.exists())
        }

        @Test
        @DisplayName("Debería leer un archivo JSON correctamente")
        fun readFile() {

            storageJson.writeToFile(tempFile, listOf(vehiculoElectrico, vehiculoPublico, vehiculoMotor))

            val result = storageJson.readFromFile(tempFile)

            assertTrue(result.isOk)
            assertEquals(3, result.value.size)
            assertEquals("Tesla", result.value[0].marca)
        }
    }

    @Nested
    @DisplayName("Casos incorrectos")
    inner class CasosIncorrectos {

        @Test
        @DisplayName("El archivo no debería existir")
        fun archivoNoExiste() {
            val file = File("ruta_invalida/vehiculos.json")
            val result = storageJson.readFromFile(file)

            assertTrue(result.isErr)
            assertTrue(result.getError() is VehiculoError.VehiculoStorageError)
        }

        @Test
        @DisplayName("El contenido del archivo no es un JSON válido")
        fun contenidoInvalido() {
            tempFile.writeText("contenido inválido que no es JSON")
            val result = storageJson.readFromFile(tempFile)

            assertTrue(result.isErr)
            assertTrue(result.getError() is VehiculoError.VehiculoStorageError)
        }

        @Test
        @DisplayName("Debería fallar al intentar escribir en un directorio inexistente o sin permisos")
        fun writeFileSinPermisos() {
            val dir = File("bloqueado")
            if (dir.exists()) dir.deleteRecursively()

            val file = File(dir, "vehiculos.json")
            val result = storageJson.writeToFile(file, listOf(vehiculoElectrico))

            assertTrue(result.isErr)
            assertTrue(result.getError() is VehiculoError.VehiculoStorageError)
        }
    }
}
