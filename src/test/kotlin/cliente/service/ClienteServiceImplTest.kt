package cliente.service

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import dev.kkarrasmil80.gestoritv.cliente.error.ClienteError
import dev.kkarrasmil80.gestoritv.cliente.models.Cliente
import dev.kkarrasmil80.gestoritv.cliente.repositories.ClienteRepositoryImpl
import dev.kkarrasmil80.gestoritv.cliente.service.ClienteServiceImpl
import dev.kkarrasmil80.gestoritv.cliente.validator.ClienteValidator
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.kotlin.*
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class ClienteServiceImplTest {

    private val repository: ClienteRepositoryImpl = mock()
    private val validator: ClienteValidator = mock()

    private val service = ClienteServiceImpl(repository, validator)

    private val cliente = Cliente(
        id = 1,
        nombre = "Juan",
        email = "juan@kkarra.com",
        password = "1234SB"
    )

    @Nested
    @DisplayName("Casos Correctos")
    inner class CasosCorrectos {

        @Test
        @DisplayName("El usuario deberia de poder logearse si sus credenciales coinciden")
        fun loginOkSuccess() {

            whenever(repository.findByEmail(cliente.email)).thenReturn(cliente)

            val result = service.loginOk(cliente.email, cliente.password)

            assertTrue(result.isOk)

            verify(repository, times(1)).findByEmail(cliente.email)
        }

        @Test
        @DisplayName("Deberia de devolver un email")
        fun findByEmailSuccess() {

            whenever(repository.findByEmail(cliente.email)).thenReturn(cliente)

            val result = service.findByEmail(cliente.email)

            assertTrue(result.isOk)
            assertEquals(cliente, result.value)

            verify(repository, times(1)).findByEmail(cliente.email)
        }
    }

    @Nested
    @DisplayName("Casos Incorrectos")
    inner class CasosIncorrectos {

        @Test
        @DisplayName("Debería de fallar cuando el email no existe")
        fun loginEmailNotFound() {

            whenever(repository.findByEmail("noexiste@kkarra.com")).thenReturn(null)

            val result = service.loginOk("emailFalso@kkarra.com", "123456")

            assertTrue(result.isErr)
            val error = (result.isErr)
            assertEquals("El email no existe",result.error.message)

            verify(repository, times(1)).findByEmail("emailFalso@kkarra.com")
        }

        @Test
        @DisplayName("Debería devolver dar error cuando la contraseña es incorrecta")
        fun loginPasswordIncorrecto() {

            whenever(repository.findByEmail(cliente.email)).thenReturn(cliente)

            val result = service.loginOk(cliente.email, "contraseñaInvalida")

            assertTrue(result.isErr)
            assertEquals("Contraseña incorrecta", result.error.message)

            verify(repository, times(1)).findByEmail(cliente.email)
        }

        @Test
        @DisplayName("findByEmail debería devolver Err cuando el email no existe")
        fun findByEmailNotFound() {

            whenever(repository.findByEmail("emailFalso@kkarra.com")).thenReturn(null)

            val result = service.findByEmail("emailFalso@kkarra.com")

            assertTrue(result.isErr)
            assertEquals("El email no existe", result.error.message)

            verify(repository, times(1)).findByEmail("emailFalso@kkarra.com")
        }
    }
}
