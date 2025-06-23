package vehiculo.service

import com.github.benmanes.caffeine.cache.Cache
import dev.kkarrasmil80.gestoritv.vehiculo.dao.VehiculoEntity
import dev.kkarrasmil80.gestoritv.vehiculo.mapper.toModel
import dev.kkarrasmil80.gestoritv.vehiculo.models.Vehiculo
import dev.kkarrasmil80.gestoritv.vehiculo.repositories.VehiculoRepository
import dev.kkarrasmil80.gestoritv.vehiculo.repositories.VehiculoRepositoryImpl
import dev.kkarrasmil80.gestoritv.vehiculo.service.VehiculoService
import dev.kkarrasmil80.gestoritv.vehiculo.service.VehiculoServiceImpl
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.never
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.*
import kotlin.math.truncate

@ExtendWith(MockitoExtension::class)
class VehiculoServiceImplTest {

    private val repository : VehiculoRepositoryImpl = mock()
    private val cache : Cache<Int, Vehiculo> = mock()
    private val service = VehiculoServiceImpl(repository, cache)

    private val vehiculoEntity = VehiculoEntity(
        id = 100,
        matricula = "ZZZ999",
        marca = "Tesla",
        modelo = "Model S",
        anio = 2022,
        tipo = "electrico",
        consumo = "15kWh/100km",
        cilindrada = null,
        capacidad = null,
        neumaticos = true,
        bateria = true,
        frenos = true,
        aceite = 5,
    )

    @Nested
    @DisplayName("Casos correctos")
    inner class CasosCorrectos {

        @Test
        @DisplayName("Deberia de devolver una lista de vehiculos")
        fun findAll() {

            whenever(repository.findAll()).thenReturn(listOf(vehiculoEntity.toModel()))

            val result = service.findAll()

            assertTrue(result.isOk)

            verify(repository, times(1)).findAll()


        }

        @Test
        @DisplayName("Deberia de devolver un identificador")
        fun findById() {

            whenever(cache.getIfPresent(vehiculoEntity.id)).thenReturn(vehiculoEntity.toModel())
            whenever(repository.findById(vehiculoEntity.id)).thenReturn(vehiculoEntity.toModel())

            val result = service.findById(vehiculoEntity.id)


            assertTrue(result.isOk)

            verify(repository, times(1)).findById(vehiculoEntity.id)
            verify(cache, times(1)).getIfPresent(vehiculoEntity.id)
        }

        @Test
        @DisplayName("Debería de poder guardar un vehiculo")
        fun save() {

            val model = vehiculoEntity.toModel()

            whenever(repository.save(model)).thenReturn(1)

            val result = service.save(model)

            assertTrue(result.isOk)

            verify(repository, times(1)).save(model)
            verify(cache, times(1)).put(vehiculoEntity.id, model)

        }

        @Test
        @DisplayName("Deberia de borrar un vehiculo")
        fun deleteById() {

            whenever(repository.deleteById(100)).thenReturn(1)

            val result = service.deleteById(100)

            assertTrue(result.isOk)

            verify(repository, times(1)).deleteById(100)
            verify(cache, times(1)).invalidate(vehiculoEntity.id)

        }

        @Test
        @DisplayName("Deberia de borrar los vehiculos")
        fun deleteAll() {

            whenever(repository.deleteAll()).thenReturn(1)

            val result = service.deleteAll()

            assertTrue(result.isOk)

            verify(repository, times(1)).deleteAll()
            verify(cache, times(1)).invalidateAll()
        }
    }

    @Nested
    @DisplayName("Casos incorrectos")
    inner class CasosIncorrectos {

        @Test
        @DisplayName("Debería fallar al buscar todos los vehículos si hay error en el repositorio")
        fun findAllIncorrecto() {
            whenever(repository.findAll()).thenReturn(emptyList())

            val result = service.findAll()

            assertTrue(result.isOk)

            verify(repository, times(1)).findAll()
        }


        @Test
        @DisplayName("Debería fallar al eliminar un vehículo si el repositorio devuelve 0")
        fun deleteByIdIncorrecto() {
            whenever(repository.deleteById(vehiculoEntity.id)).thenReturn(0)

            val result = service.deleteById(vehiculoEntity.id)

            assertTrue(result.isErr)
            verify(repository, times(1)).deleteById(vehiculoEntity.id)
            verify(cache, never()).invalidate(vehiculoEntity.id)
        }

        @Test
        @DisplayName("Debería fallar al eliminar todos los vehículos si el repositorio devuelve 0")
        fun deleteAllIncorrecto() {
            whenever(repository.deleteAll()).thenReturn(0)

            val result = service.deleteAll()

            assertTrue(result.isErr)

            verify(repository, times(1)).deleteAll()
            verify(cache, never()).invalidateAll()
        }
    }


}