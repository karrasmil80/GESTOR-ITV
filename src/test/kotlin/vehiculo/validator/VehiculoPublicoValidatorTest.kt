package vehiculo.validator

import dev.kkarrasmil80.gestoritv.vehiculo.models.VehiculoElectrico
import dev.kkarrasmil80.gestoritv.vehiculo.models.VehiculoMotor
import dev.kkarrasmil80.gestoritv.vehiculo.models.VehiculoPublico
import dev.kkarrasmil80.gestoritv.vehiculo.validator.VehiculoElectricoValidator
import dev.kkarrasmil80.gestoritv.vehiculo.validator.VehiculoPublicoValidator
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class VehiculoPublicoValidatorTest {

    private val validator = VehiculoPublicoValidator()

    private val vehiculo = VehiculoPublico(
        id = 100,
        matricula = "1234-ZZZ",
        marca = "Tesla",
        modelo = "Model S",
        anio = 2022,
        tipo = "electrico",
        capacidad = 1,
        neumaticos = true,
        bateria = true,
        frenos = true,
    )

    @Nested
    @DisplayName("Casos correctos")
    inner class CasosCorrectos {
        @Test
        @DisplayName("Vehiculo correcto")
        fun vehiculoOk() {

            val result = validator.validate(vehiculo)

            assertTrue(result.isOk, "Vehiculo correctamente validado")
            assertEquals(vehiculo, result.value, "El validador debe de devolver el mismo vehiculo")


        }
    }

    @Nested
    @DisplayName("Casos incorrectos")
    inner class CasosIncorrectos {

        @Test
        @DisplayName("ID inválido")
        fun idInvalido() {
            val vehiculoInvalid = VehiculoPublico(
                id = -1,
                matricula = "1234-ZZZ",
                marca = "Tesla",
                modelo = "Model S",
                anio = 2022,
                tipo = "electrico",
                capacidad = 1,
                neumaticos = true,
                bateria = true,
                frenos = true
            )

            val result = validator.validate(vehiculoInvalid)
            assertTrue(result.isErr)
            assertEquals("ID inválido", result.error.message)
        }

        @Test
        @DisplayName("Matrícula en blanco")
        fun matriculaBlanca() {
            val vehiculoInvalid = VehiculoPublico(
                id = 100,
                matricula = "",
                marca = "Tesla",
                modelo = "Model S",
                anio = 2022,
                tipo = "electrico",
                capacidad = 1,
                neumaticos = true,
                bateria = true,
                frenos = true
            )

            val result = validator.validate(vehiculoInvalid)
            assertTrue(result.isErr)
            assertEquals("Matrícula inválida", result.error.message)
        }

        @Test
        @DisplayName("Matrícula con formato incorrecto")
        fun matriculaFormatoIncorrecto() {
            val vehiculoInvalid = VehiculoPublico(
                id = 100,
                matricula = "1234ZZZ",
                marca = "Tesla",
                modelo = "Model S",
                anio = 2022,
                tipo = "electrico",
                capacidad = 1,
                neumaticos = true,
                bateria = true,
                frenos = true
            )

            val result = validator.validate(vehiculoInvalid)
            assertTrue(result.isErr)
            assertEquals("La matrícula debe seguir el patrón especificado", result.error.message)
        }

        @Test
        @DisplayName("Modelo vacío")
        fun modeloVacio() {
            val vehiculoInvalid = VehiculoPublico(
                id = 100,
                matricula = "1234-ZZZ",
                marca = "Tesla",
                modelo = "",
                anio = 2022,
                tipo = "electrico",
                capacidad = 1,
                neumaticos = true,
                bateria = true,
                frenos = true
            )

            val result = validator.validate(vehiculoInvalid)
            assertTrue(result.isErr)
            assertEquals("Modelo inválido", result.error.message)
        }

        @Test
        @DisplayName("Año superior a 2025")
        fun anioFuturo() {
            val vehiculoInvalid = VehiculoPublico(
                id = 100,
                matricula = "1234-ZZZ",
                marca = "Tesla",
                modelo = "Model S",
                anio = 2029,
                tipo = "electrico",
                capacidad = 1,
                neumaticos = true,
                bateria = true,
                frenos = true
            )

            val result = validator.validate(vehiculoInvalid)
            assertTrue(result.isErr)
            assertEquals("El vehículo no puede ser mayor a la fecha actual", result.error.message)
        }

        @Test
        @DisplayName("Marca vacía")
        fun marcaVacia() {
            val vehiculoInvalid = VehiculoPublico(
                id = 100,
                matricula = "1234-ZZZ",
                marca = "",
                modelo = "Model S",
                anio = 2022,
                tipo = "electrico",
                capacidad = 1,
                neumaticos = true,
                bateria = true,
                frenos = true
            )

            val result = validator.validate(vehiculoInvalid)
            assertTrue(result.isErr)
            assertEquals("Marca inválida", result.error.message)
        }

        @Test
        @DisplayName("Tipo vacío")
        fun tipoVacio() {
            val vehiculoInvalid = VehiculoPublico(
                id = 100,
                matricula = "1234-ZZZ",
                marca = "Tesla",
                modelo = "Model S",
                anio = 2022,
                tipo = "",
                capacidad = 1,
                neumaticos = true,
                bateria = true,
                frenos = true
            )

            val result = validator.validate(vehiculoInvalid)
            assertTrue(result.isErr)
            assertEquals("El tipo no puede estar en blanco", result.error.message)
        }

        @Test
        @DisplayName("el consumo no puede estar vacio")
        fun capacidadIgualACero() {
            val vehiculoInvalid = VehiculoPublico(
                id = 100,
                matricula = "1234-ZZZ",
                marca = "Tesla",
                modelo = "Model S",
                anio = 2022,
                tipo = "electrico",
                capacidad = 0,
                neumaticos = true,
                bateria = true,
                frenos = true
            )

            val result = validator.validate(vehiculoInvalid)

            assertTrue(result.isErr)
            assertEquals("La capacidad no puede ser 0", result.error.message)
        }

        @Test
        @DisplayName("El aceite no puede ser menos de 3 Litros")
        fun neumaticosInvalido() {
            val vehiculoInvalid = VehiculoPublico(
                id = 100,
                matricula = "1234-ZZZ",
                marca = "Tesla",
                modelo = "Model S",
                anio = 2022,
                tipo = "electrico",
                neumaticos = false,
                bateria = true,
                frenos = true,
                capacidad = 20,
            )

            val result = validator.validate(vehiculoInvalid)

            assertTrue(result.isErr)
            assertEquals("Los neumáticos no han pasado la prueba", result.error.message)
        }

        @Test
        @DisplayName("El aceite no puede ser menos de 3 Litros")
        fun frenosInvalido() {
            val vehiculoInvalid = VehiculoPublico(
                id = 100,
                matricula = "1234-ZZZ",
                marca = "Tesla",
                modelo = "Model S",
                anio = 2022,
                tipo = "electrico",
                neumaticos = true,
                bateria = true,
                frenos = false,
                capacidad = 20,
            )

            val result = validator.validate(vehiculoInvalid)

            assertTrue(result.isErr)
            assertEquals("Los frenos no han pasado la prueba", result.error.message)
        }

        @Test
        @DisplayName("Deberia de no pasar la prueba de los bateria")
        fun bateriaInvalida() {
            val vehiculoInvalid = VehiculoPublico(
                id = 100,
                matricula = "1234-ZZZ",
                marca = "Tesla",
                modelo = "Model S",
                anio = 2022,
                tipo = "electrico",
                neumaticos = true,
                bateria = false,
                frenos = true,
                capacidad = 20
            )

            val result = validator.validate(vehiculoInvalid)

            assertTrue(result.isErr)
            assertEquals("La batería no ha pasado la prueba", result.error.message)
        }
    }
}