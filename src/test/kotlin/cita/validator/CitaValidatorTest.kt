package cita.validator

import dev.kkarrasmil80.gestoritv.cita.error.CitaError
import dev.kkarrasmil80.gestoritv.cita.models.Cita
import dev.kkarrasmil80.gestoritv.cita.validator.CitaValidator
import dev.kkarrasmil80.gestoritv.vehiculo.models.VehiculoMotor
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.Assertions.*

class CitaValidatorTest {

    private val validator : CitaValidator = CitaValidator()

    private val vehiculoMotor = VehiculoMotor(
        id = 100,
        matricula = "1234-ZZZ",
        marca = "Tesla",
        modelo = "Model S",
        anio = 2022,
        tipo = "electrico",
        cilindrada = 1,
        neumaticos = true,
        bateria = true,
        frenos = true,
        aceite = 5
    )

    @Nested
    @DisplayName("Casos correctos")
    inner class CasosCorrectos {

        @Test
        @DisplayName("Deberia de ser una cita correcta")
        fun citaOk() {
            val cita = Cita(
                id = 1,
                fechaCita = "1970-01-01",
                hora = "00:00:00:00",
                vehiculo = vehiculoMotor
            )

            val result = validator.validate(cita)

            assertTrue(result.isOk)
        }
    }

    @Nested
    @DisplayName("Casos incorrectos")
    inner class CasosIncorrectos {

        @Test
        @DisplayName("Deberia de dar un error por que el vehiculo es nulo")
        fun vehiculoNulo() {
            val cita = Cita(
                id = 1,
                fechaCita = "1970-01-01",
                hora = "00:00:00:00",
                vehiculo = null
            )

            val result = validator.validate(cita)

            assertTrue(result.isErr)
            assertEquals("El vehiculo no puede ser nulo", result.error.message)
        }

        @Test
        @DisplayName("Deberia de dar un error por que la fecha esta en blanco")
        fun fechaIsBlank() {
            val cita = Cita(
                id = 1,
                fechaCita = "",
                hora = "00:00:00:00",
                vehiculo = vehiculoMotor
            )

            val result = validator.validate(cita)

            assertTrue(result.isErr)
            assertEquals("La fecha no puede estar en blanco", result.error.message)
        }

        @Test
        @DisplayName("Deberia de dar un error por dejar la hora en blanco")
        fun horaIsBlank() {
            val cita = Cita(
                id = 1,
                fechaCita = "1970-01-01",
                hora = "",
                vehiculo = vehiculoMotor
            )

            val result = validator.validate(cita)

            assertTrue(result.isErr)
            assertEquals("La hora no puede estar en blanco", result.error.message)
        }
    }
}