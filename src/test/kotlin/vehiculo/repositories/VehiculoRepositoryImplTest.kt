package vehiculo.repositories

import dev.kkarrasmil80.gestoritv.vehiculo.dao.VehiculoDao
import dev.kkarrasmil80.gestoritv.vehiculo.dao.VehiculoEntity
import dev.kkarrasmil80.gestoritv.vehiculo.mapper.toModel
import dev.kkarrasmil80.gestoritv.vehiculo.models.VehiculoElectrico
import dev.kkarrasmil80.gestoritv.vehiculo.repositories.VehiculoRepositoryImpl
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.whenever

@ExtendWith(MockitoExtension::class)
class VehiculoRepositoryImplTest {

    private val dao: VehiculoDao = mock()
    private val repository = VehiculoRepositoryImpl(dao)

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
    )

    @Nested
    @DisplayName("Casos correctos")
    inner class CasosCorrectos {

        @Test
        @DisplayName("Deberia de devolver una lista de vehiculos")
        fun findAll() {

            whenever(dao.getAll()).thenReturn(listOf(vehiculoEntity))

            val lista = repository.findAll()

            assertEquals(lista.size, 1)
            assertEquals(lista[0].id, vehiculoEntity.id)
            assertEquals(lista[0].matricula, "ZZZ999")
            assertEquals(lista[0].marca, "Tesla")
            assertEquals(lista[0].modelo, "Model S")
            assertEquals(lista[0].anio, 2022)

        }
    }

    @Test
    @DisplayName("Deberia devolver el identificador de un vehiculo")
    fun findById() {

        whenever(dao.getById(100)).thenReturn(vehiculoEntity)

        val resuktado = repository.findById(100)

        assertNotNull(resuktado)
        assertTrue(resuktado is VehiculoElectrico)

    }

    @Test
    @DisplayName("Deberia de insertar correctamente un vehiculo")
    fun save() {

        whenever(dao.insert(vehiculoEntity)).thenReturn(1)

        val toSave = repository.save(vehiculoEntity.toModel())

        assertNotNull(toSave)
        assertEquals(1, toSave)
    }

    @Test
    @DisplayName("Deberia de borrar correctamente de vehiculo")
    fun deleteById() {

        whenever(repository.deleteById(100)).thenReturn(1)
        val toDelete = repository.deleteById(100)

        assertNotNull(toDelete)
        assertEquals(1, toDelete)
    }

    @Test
    @DisplayName("Deberia de borrar vehiculos")
    fun deleteAll() {

        whenever(dao.deleteAll()).thenReturn(3)

        val result = repository.deleteAll()

        assertEquals(3, result)
    }

    @Nested
    @DisplayName("Casos incorrectos")
    inner class CasosIncorrectos {

        @Test
        @DisplayName("Deberia de devolver una lista vacia")
        fun findAllIncorrecto() {

        }

        @Test
        @DisplayName("Deberia de ser un id que no exista")
        fun findByIdIncorrecto() {

            whenever(dao.getById(999)).thenReturn(null)

            val result = repository.findById(999)

            assertNull(result)
        }

        @Test
        @DisplayName("Deberia de devolver 0 (no ha borrado nada) si no hay filas afectadas")
        fun deleteByIdIncorrecto() {

            whenever(dao.deleteById(999)).thenReturn(0)

            val result = repository.deleteById(999)

            assertEquals(0, result, "No existe ningun vehiculo con id 999")
        }

        @Test
        @DisplayName("Deberia de no borrar ninguna fila y devolver 0 si no hay nada que eliminar")
        fun deleteAllIncorrecto() {

            whenever(dao.deleteAll()).thenReturn(0)

            val result = repository.deleteAll()

            assertEquals(0, result, "No hay nada que borrar, 0 filas afectadas")
        }
    }

}