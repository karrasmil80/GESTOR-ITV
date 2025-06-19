package cita.service

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.get
import dev.kkarrasmil80.gestoritv.cita.error.CitaError
import dev.kkarrasmil80.gestoritv.cita.models.Cita
import dev.kkarrasmil80.gestoritv.cita.repositories.CitasRepositoryImpl
import dev.kkarrasmil80.gestoritv.cita.service.CitaServiceImpl
import dev.kkarrasmil80.gestoritv.cita.validator.CitaValidator
import dev.kkarrasmil80.gestoritv.vehiculo.models.VehiculoElectrico
import dev.kkarrasmil80.gestoritv.vehiculo.repositories.VehiculoRepositoryImpl
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExtendWith(MockitoExtension::class)
class ClienteServiceMockitoImpl {
    private val repository: CitasRepositoryImpl = mock()
    private val repositoryVehiculo: VehiculoRepositoryImpl = mock()
    private val validator: CitaValidator = mock()

    private val service = CitaServiceImpl(repository, repositoryVehiculo, validator)

    private val vehiculo = VehiculoElectrico(
        id = 1,
        matricula = "1234ABC",
        marca = "Tesla",
        modelo = "Model 3",
        anio = 2023,
        consumo = "12kWh/100km",
        bateria = true,
        neumaticos = true,
        frenos = true,
        tipo = "electrico"
    )

    private val cita = Cita(
        id = 1,
        fechaCita = "2025-10-10",
        hora = "10:00",
        vehiculo = vehiculo
    )

    @Nested
    @DisplayName("Casos correctos")
    inner class CasosCorrectos {

        @Test
        @DisplayName("Debería devolver todas las citas")
        fun getAll() {

            whenever(repository.getAll()).thenReturn(listOf(cita))

            val result = service.getAll()

            assertTrue(result.isOk)
            verify(repository, times(1)).getAll()
        }

        @Test
        @DisplayName("Debería devolver una cita por ID")
        fun getById() {

            whenever(repository.getById(1)).thenReturn(cita)

            val result = service.getById(1)

            assertTrue(result.isOk)
            assertEquals(cita, result.get())

            verify(repository, times(1)).getById(1)
        }

        @Test
        @DisplayName("Debería insertar correctamente una cita")
        fun insert() {

            whenever(validator.validate(any())).thenReturn(Ok(cita))
            whenever(repositoryVehiculo.save(any())).thenReturn(vehiculo.id)
            whenever(repository.fechaYHoraCita(cita.hora, cita.fechaCita)).thenReturn(false)
            whenever(repository.insert(any())).thenReturn(99)

            val result = service.insert(cita)

            assertTrue(result.isOk)
            assertEquals(99, result.get()?.id)
            verify(repository, times(1)).insert(any())
        }

        @Test
        @DisplayName("Debería eliminar por ID correctamente")
        fun deleteById() {
            whenever(repository.deleteById(1)).thenReturn(1)

            val result = service.deleteById(1)

            assertTrue(result.isOk)
            verify(repository, times(1)).deleteById(1)
        }

        @Test
        @DisplayName("Debería eliminar todas las citas")
        fun deleteAll() {

            whenever(repository.deleteAll()).thenReturn(3)

            val result = service.deleteAll()

            assertTrue(result.isOk)
            assertEquals(3, result.get())

            verify(repository, times(1)).deleteAll()
        }
    }

    @Nested
    @DisplayName("Casos incorrectos")
    inner class CasosIncorrectos {

        @Test
        @DisplayName("Debería fallar si no existe la cita por ID")
        fun getByIdFalla() {

            whenever(repository.getById(1)).thenReturn(null)

            val result = service.getById(1)

            assertTrue(result.isErr)
            assertEquals("No se ha podido encontrar la cita por que no existe el id", result.error.message)
        }

        @Test
        @DisplayName("Debería fallar si ya existe una cita en la misma hora")
        fun insertFallaPorMismaHora() {

            whenever(validator.validate(any())).thenReturn(Ok(cita))
            whenever(repositoryVehiculo.save(any())).thenReturn(vehiculo.id)
            whenever(repository.fechaYHoraCita(cita.hora, cita.fechaCita)).thenReturn(true)

            val result = service.insert(cita)

            assertTrue(result.isErr)
            assertEquals("Ya existe una cita a esa hora", result.error.message)
        }

        @Test
        @DisplayName("Debería fallar si la validación es errónea")
        fun insertFailsPorValidacion() {
            whenever(validator.validate(any())).thenReturn(Err(CitaError.CitaValidatorError("Error")))

            val result = service.insert(cita)

            assertTrue(result.isErr)
            assertEquals("Error al validar la cita, no esta bien formada o faltan datos", result.error.message)
        }

        @Test
        @DisplayName("Debería fallar si no elimina ninguna cita por ID")
        fun deleteByIdFails() {
            whenever(repository.deleteById(1)).thenReturn(0)
            val result = service.deleteById(1)
            assertTrue(result.isErr)
            assertEquals("No se ha podido encontrar la cita", result.error.message)
        }

        @Test
        @DisplayName("Debería devolver lista vacía")
        fun getAllEmpty() {
            whenever(repository.getAll()).thenReturn(emptyList())

            val result = service.getAll()

            assertTrue(result.isOk)
            assertEquals(0, result.get()?.size)
        }
    }
}