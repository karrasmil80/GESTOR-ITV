package cita.repositories

import dev.kkarrasmil80.gestoritv.cita.dao.CitaDao
import dev.kkarrasmil80.gestoritv.cita.dao.CitaEntity
import dev.kkarrasmil80.gestoritv.cita.mapper.toEntity
import dev.kkarrasmil80.gestoritv.cita.models.Cita
import dev.kkarrasmil80.gestoritv.cita.repositories.CitasRepositoryImpl
import dev.kkarrasmil80.gestoritv.vehiculo.dao.VehiculoDao
import dev.kkarrasmil80.gestoritv.vehiculo.mapper.toEntity
import dev.kkarrasmil80.gestoritv.vehiculo.models.Vehiculo
import dev.kkarrasmil80.gestoritv.vehiculo.models.VehiculoMotor
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import org.junit.jupiter.api.Assertions.*

@ExtendWith(MockitoExtension::class)
class CitaRepositoryTest {
    private val citaDao: CitaDao = mock()
    private val vehiculoDao: VehiculoDao = mock()
    private val repository = CitasRepositoryImpl(citaDao, vehiculoDao)

    private val vehiculo = VehiculoMotor(
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

    private val citaEntity = CitaEntity(
        id = 10,
        fechaCita = "2025-06-20",
        hora = "09:00:00",
        vehiculoId = 1
    )

    private val citaModel = Cita(
        id = 10,
        fechaCita = "2025-06-20",
        hora = "09:00:00",
        vehiculo = vehiculo
    )

    @Nested
    @DisplayName("Casos correctos")
    inner class CasosCorrectos {

        @Test
        @DisplayName("Debería devolver una lista de citas con vehículos")
        fun testGetAll() {
            whenever(citaDao.getAll()).thenReturn(listOf(citaEntity))
            whenever(vehiculoDao.getById(citaEntity.vehiculoId)).thenReturn(vehiculo.toEntity())

            val result = repository.getAll()

            assertEquals(1, result.size)
            assertEquals(citaModel.id, result[0].id)
            assertEquals(citaModel.vehiculo!!.marca, result[0].vehiculo!!.marca)
        }

        @Test
        @DisplayName("Debería devolver una cita por id")
        fun testGetById() {
            whenever(citaDao.getById(10)).thenReturn(citaEntity)
            whenever(vehiculoDao.getById(citaEntity.vehiculoId)).thenReturn(vehiculo.toEntity())

            val result = repository.getById(10)

            assertNotNull(result)
            assertEquals(citaModel.id, result.id)
            assertEquals(citaModel.vehiculo!!.modelo, result.vehiculo!!.modelo)
        }

        @Test
        @DisplayName("Debería insertar una cita y devolver el id generado")
        fun testInsert() {
            whenever(citaDao.insert(citaModel.toEntity())).thenReturn(citaModel.id)

            val result = repository.insert(citaModel)

            assertEquals(citaModel.id, result)
        }

        @Test
        @DisplayName("Debería eliminar una cita por id")
        fun testDeleteById() {
            whenever(citaDao.deleteById(10)).thenReturn(1)

            val result = repository.deleteById(10)

            assertEquals(1, result)
        }

        @Test
        @DisplayName("Debería eliminar todas las citas")
        fun testDeleteAll() {
            whenever(citaDao.deleteAll()).thenReturn(3)

            val result = repository.deleteAll()

            assertEquals(3, result)
        }
    }

    @Nested
    @DisplayName("Casos incorrectos")
    inner class CasosIncorrectos {

        @Test
        @DisplayName("Debería devolver una lista vacía cuando no hay citas")
        fun testGetAllEmpty() {
            whenever(citaDao.getAll()).thenReturn(emptyList())

            val result = repository.getAll()

            assertEquals(0, result.size)
        }


        @Test
        @DisplayName("Debería devolver 0 al intentar eliminar cita con id inexistente")
        fun testDeleteByIdNoExists() {
            whenever(citaDao.deleteById(999)).thenReturn(0)

            val result = repository.deleteById(999)

            assertEquals(0, result)
        }

        @Test
        @DisplayName("Debería devolver 0 al eliminar todas las citas si no hay ninguna")
        fun testDeleteAllEmpty() {
            whenever(citaDao.deleteAll()).thenReturn(0)

            val result = repository.deleteAll()

            assertEquals(0, result)
        }
    }
}