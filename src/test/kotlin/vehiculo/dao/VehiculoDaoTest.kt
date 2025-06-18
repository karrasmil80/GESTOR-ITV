package vehiculo.dao

import dev.kkarrasmil80.gestoritv.database.JdbiManager
import dev.kkarrasmil80.gestoritv.vehiculo.utils.provideVehiculoDao
import dev.kkarrasmil80.gestoritv.vehiculo.dao.VehiculoDao
import dev.kkarrasmil80.gestoritv.vehiculo.dao.VehiculoEntity
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class VehiculoDaoTest {

    private lateinit var dao: VehiculoDao
    private lateinit var jdbi: JdbiManager

    private val vehiculo = VehiculoEntity(
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

    @BeforeAll
    fun setupDatabase() {
        jdbi = JdbiManager(
            databaseUrl = "jdbc:h2:mem:itv;DB_CLOSE_DELAY=-1",
            databaseInitTables = true,
            databaseInitData = true,
            databaseLogger = false
        )
        dao = provideVehiculoDao(jdbi.jdbi)
    }

    @BeforeEach
    fun clear() {
        dao.deleteAll()
        jdbi.executeSqlScriptFromResources("data.sql")
    }

    @Nested
    @DisplayName("Casos correctos")
    inner class CasosCorrectos {

        @Test
        fun getAll() {
            val vehiculos = dao.getAll()
            assertEquals(3, vehiculos.size, "Debe haber 3 vehículos")
        }

        @Test
        fun getById() {
            val vehiculoGetById = dao.getById(1)
            assertNotNull(vehiculoGetById)
            assertEquals("ABC123", vehiculoGetById.matricula)
        }

        @Test
        fun insert() {
            val insertId = dao.insert(vehiculo)
            val inserted = dao.getById(vehiculo.id)

            assertAll(
                { assertNotNull(inserted, "Debe existir el vehículo insertado") },
                { assertEquals(vehiculo.id, inserted.id) },
                { assertEquals("Tesla", inserted.marca) },
                { assertEquals(null, inserted.capacidad) }
            )
        }

        @Test
        fun deleteById() {
            dao.insert(vehiculo)
            val delete = dao.deleteById(vehiculo.id)
            val deleted = dao.getById(vehiculo.id)

            assertEquals(1, delete, "Debe eliminar una fila")
            assertNull(deleted, "El vehículo debe haber sido eliminado")
        }
    }

    @Test
    @DisplayName("Debe de borrarlo todo sobre un vehiculo")
    fun deleteAll() {

        dao.deleteAll()

        val insert = dao.insert(vehiculo)

        val deleteAll = dao.deleteAll()

        val findAll = dao.getAll()

        assertAll(
            { assertEquals(1, deleteAll, "deberia de eliminar dos filas") },
            { assertTrue(findAll.isEmpty(), "No deben de quedar vehiculos en la lista") },
        )
    }

    @Nested
    @DisplayName("Casos incorrectos")
    inner class CasosIncorrectos {

        @Test
        @DisplayName("Deberia de existir el identificador : 1")
        fun getById_incorrecto() {

            val vehiculoGetById = dao.getById(1)

            assertNotNull(vehiculoGetById, "Deberia de existir el identificador con el numero 1")

        }

        @Test
        @DisplayName("Deberia de devolver un identificador y no un objeto del tipo Vehiculo")
        fun insert_incorrecto() {
            val insertVehiculo = dao.insert(vehiculo)
            val getVehiculoId = dao.getById(insertVehiculo)

            assertNotNull(getVehiculoId, "El vehículo insertado no debe ser nulo")
        }

        @Test
        @DisplayName("Deberia de eliminar una fila, no dos")
        fun deleteAll_incorrecto() {
            dao.deleteAll()

            val insert = dao.insert(vehiculo)

            val deleteAll = dao.deleteAll()

            val findAll = dao.getAll()

            assertAll(

                { assertEquals(1, deleteAll, "deberia de eliminar dos filas") },
                { assertTrue(findAll.isEmpty(), "No deben de quedar vehiculos en la lista") },
            )
        }
    }

}