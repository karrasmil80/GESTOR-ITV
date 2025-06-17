package cita.dao

import dev.kkarrasmil80.gestoritv.cita.dao.CitaDao
import dev.kkarrasmil80.gestoritv.cita.dao.CitaEntity
import dev.kkarrasmil80.gestoritv.cita.utils.provideCitaDao
import dev.kkarrasmil80.gestoritv.database.JdbiManager
import dev.kkarrasmil80.gestoritv.vehiculo.dao.VehiculoEntity
import dev.kkarrasmil80.gestoritv.vehiculo.models.Vehiculo
import dev.kkarrasmil80.gestoritv.vehiculo.models.VehiculoMotor
import dev.kkarrasmil80.gestoritv.vehiculo.utils.provideVehiculoDao
import org.jdbi.v3.core.Jdbi
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CitaDaoTest {

    private lateinit var dao: CitaDao
    private lateinit var jdbi : JdbiManager

    @BeforeAll
    fun setupDatabase() {
        jdbi = JdbiManager(
            databaseUrl = "jdbc:h2:mem:itv;DB_CLOSE_DELAY=-1",
            databaseInitTables = true,
            databaseInitData = true,
            databaseLogger = false
        )
        dao = provideCitaDao(jdbi.jdbi)
    }

    @BeforeEach
    fun clear() {
        dao.deleteAll()
        jdbi.executeSqlScriptFromResources("data.sql")
    }

    /*
    private val vehiculo = VehiculoMotor(
        id = 100,
        matricula = "ZZZ999",
        marca = "Tesla",
        modelo = "Model S",
        anio = 2022,
        tipo = "motor",
        cilindrada = 1
    )
     */

    private val cita = CitaEntity(
        id = 100,
        fechaCita = "2025-06-20",
        hora = "10:00:00",
        vehiculoId = 999,
    )

    @Nested
    @DisplayName("Casos correctos")
    inner class CasosCorrectos {
        @Test
        @DisplayName("Deberia de devolver una lista")
        fun getAll() {

            val getAll = dao.getAll()

            assertEquals(3, getAll.size)
        }

        @Test
        @DisplayName("Deberia de devolver un identificar de cita")
        fun getById() {

            val getById = dao.getById(1)

            assertNotNull(getById)
            assertEquals(1, getById.id)

        }

        @Test
        @DisplayName("Debería insertar una cita")
        fun insert() {

            dao.insert(cita)

            val getById = dao.getById(cita.id)

            assertAll(
                { assertNotNull(getById, "Debería insertar una cita y no ser nulo") },
                { assertEquals(cita.id, getById.id) },
                { assertEquals(cita.fechaCita, getById.fechaCita) },
                { assertEquals(cita.hora, getById.hora) }
            )
        }

        @Test
        @DisplayName("Deberia de borrar el identificador de una cita")
        fun deleteById() {

            val delete = dao.deleteById(1)

            val deleted = dao.getById(1)

            assertEquals(1, delete, "Deberia de haber eliminado una fila")
            assertNull(deleted, "El vehiculo deberia de haberse eliminado")
        }

        @Test
        @DisplayName("Deberia de borrar citas")
        fun deleteAll() {

            val insert = dao.insert(cita)

            val deleteAll = dao.deleteAll()

            val findAll = dao.getAll()

            assertAll(
                { assertEquals(4, deleteAll, "Deberia de haber afecctado a una fila") },
                { assertTrue(findAll.isEmpty(), "Deberia de devolver una lista vacia")}
            )
        }
    }

    @Nested
    @DisplayName("Casos incorrectos")
    inner class CasosIncorrectos {

        @Test
        @DisplayName("Deberia de devolver un identificador y no un objeto del tipo Vehiculo")
        fun insert_incorrecto() {
            val insert = dao.insert(cita)
            val getCitaId = dao.getById(insert)

            assertNotNull(getCitaId, "El vehículo insertado no debe ser nulo")
        }

        @Test
        @DisplayName("Deberia de existir el identificador : 1")
        fun getById_incorrecto() {

            val citaGetById = dao.getById(1)

            assertNotNull(citaGetById, "Deberia de existir el identificador con el numero 1")

        }

        @Test
        @DisplayName("Deberia de borrar citas")
        fun deleteAll() {

            val insert = dao.insert(cita)

            val deleteAll = dao.deleteAll()

            val findAll = dao.getAll()

            assertAll(
                { assertEquals(4, deleteAll, "Deberia de haber afecctado a una fila") },
                { assertTrue(findAll.isEmpty(), "Deberia de devolver una lista vacia")}
            )
        }


    }
}