package cliente.dao


import dev.kkarrasmil80.gestoritv.cliente.dao.ClienteDao
import dev.kkarrasmil80.gestoritv.cliente.dao.ClienteEntity
import dev.kkarrasmil80.gestoritv.database.JdbiManager
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ClienteDaoTest {

    private lateinit var dao: ClienteDao
    private lateinit var jdbi: JdbiManager

    @BeforeAll
    fun setupDatabase() {
        jdbi = JdbiManager(
            databaseUrl = "jdbc:h2:mem:itv-clientes;DB_CLOSE_DELAY=-1",
            databaseInitTables = true,
            databaseInitData = true,
            databaseLogger = false
        )
        dao = jdbi.jdbi.onDemand(ClienteDao::class.java)
    }

    @BeforeEach
    fun clearAndSeed() {
        jdbi.executeSqlScriptFromResources("data.sql")
    }

    private val cliente = ClienteEntity(
        id = 999,
        nombre = "Pedro",
        email = "pedro@test.com",
        password = "1234"
    )

    @Nested
    @DisplayName("Casos correctos")
    inner class CasosCorrectos {

        @Test
        @DisplayName("Debería encontrar un cliente por email")
        fun findByEmail() {

            val result = dao.findByEmail("pablo@kkarra.com")
            assertNotNull(result)
            assertEquals("pablo@kkarra.com", result?.email)
        }

        @Test
        @DisplayName("Debería iniciar sesión correctamente con email y contraseña")
        fun loginOk() {
            val result = dao.loginOk("pablo@kkarra.com", "123456")
            assertNotNull(result)
            assertEquals("pablo@kkarra.com", result?.email)
        }
    }

    @Nested
    @DisplayName("Casos incorrectos")
    inner class CasosIncorrectos {

        @Test
        @DisplayName("No debería encontrar un cliente con email inexistente")
        fun findByEmailFalla() {
            val result = dao.findByEmail("noexiste@kkarra.com")
            assertNull(result)
        }

        @Test
        @DisplayName("No debería iniciar sesión si la contraseña es incorrecta")
        fun loginOkFallaPorContraseñaIncorrecta() {
            val result = dao.loginOk("test1@kkarra.com", "wrong-password")
            assertNull(result)
        }

        @Test
        @DisplayName("No debería iniciar sesión si el email no existe")
        fun loginFallaPorEmailNoEcontrado() {
            val result = dao.loginOk("noexiste@kkarra.com", "1234")
            assertNull(result)
        }
    }
}
