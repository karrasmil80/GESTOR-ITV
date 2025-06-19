package cliente.repositories

import dev.kkarrasmil80.gestoritv.cliente.dao.ClienteDao
import dev.kkarrasmil80.gestoritv.cliente.dao.ClienteEntity
import dev.kkarrasmil80.gestoritv.cliente.mapper.toModel
import dev.kkarrasmil80.gestoritv.cliente.repositories.ClienteRepositoryImpl
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.kotlin.*
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class ClienteRepositoryMockitoTest {

    private val dao: ClienteDao = mock()
    private val repository = ClienteRepositoryImpl(dao)

    private val clienteEntity = ClienteEntity(
        id = 1,
        nombre = "Juan",
        email = "juan@kkarra.com",
        password = "1234SB"
    )

    private val clienteModel = clienteEntity.toModel()

    @Nested
    @DisplayName("Casos Correctos")
    inner class CasosCorrectos {

        @Test
        @DisplayName("findByEmail debería devolver cliente")
        fun findByEmailSuccess() {

            whenever(dao.findByEmail(clienteEntity.email)).thenReturn(clienteEntity)

            val result = repository.findByEmail(clienteEntity.email)

            assertNotNull(result)
            assertEquals(clienteModel, result)

            verify(dao, times(1)).findByEmail(clienteEntity.email)
        }

        @Test
        @DisplayName("login debería devolver cliente cuando las credenciales son correctas")
        fun loginSuccess() {

            whenever(dao.loginOk(clienteEntity.email, clienteEntity.password)).thenReturn(clienteEntity)

            val result = repository.login(clienteEntity.email, clienteEntity.password)

            assertNotNull(result)
            assertEquals(clienteModel, result)

            verify(dao, times(1)).loginOk(clienteEntity.email, clienteEntity.password)
        }
    }

    @Nested
    @DisplayName("Casos Incorrectos")
    inner class CasosIncorrectos {

        @Test
        @DisplayName("findByEmail debería devolver null cuando no existe cliente")
        fun findByEmailNotFound() {

            whenever(dao.findByEmail("noexiste@kkarra.com")).thenReturn(null)

            val result = repository.findByEmail("noexiste@kkarra.com")

            assertNull(result)

            verify(dao, times(1)).findByEmail("noexiste@kkarra.com")
        }

        @Test
        @DisplayName("login debería devolver null cuando las credenciales son incorrectas")
        fun loginFail() {

            whenever(dao.loginOk("usuarioIncorrecto@kkarra.com", "contraseñaIncorrecta")).thenReturn(null)

            val result = repository.login("usuarioIncorrecto@kkarra.com", "contraseñaIncorrecta")

            assertNull(result)

            verify(dao, times(1)).loginOk("usuarioIncorrecto@kkarra.com", "contraseñaIncorrecta")
        }
    }
}
