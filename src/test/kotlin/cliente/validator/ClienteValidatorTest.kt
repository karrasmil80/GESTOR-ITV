package cliente.validator

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import dev.kkarrasmil80.gestoritv.cliente.error.ClienteError
import dev.kkarrasmil80.gestoritv.cliente.models.Cliente
import dev.kkarrasmil80.gestoritv.cliente.validator.ClienteValidator
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@DisplayName("Tests ClienteValidator")
class ClienteValidatorTest {

    private val validator = ClienteValidator()

    private val validCliente = Cliente(
        id = 1,
        nombre = "Juan",
        email = "juan@kkarra.com",
        password = "123456"
    )

    @Nested
    @DisplayName("Casos Correctos")
    inner class CasosCorrectos {

        @Test
        @DisplayName("Debería validar correctamente un cliente válido")
        fun validateClienteCorrecto() {
            val result = validator.validate(validCliente)

            assertTrue(result.isOk)
        }
    }

    @Nested
    @DisplayName("Casos Incorrectos")
    inner class CasosIncorrectos {

        @Test
        @DisplayName("Nombre en blanco debe fallar")
        fun nombreEnBlanco() {
            val cliente = Cliente(
                id = 1,
                nombre = "",
                email = "juan@kkarra.com",
                password = "123456"
            )

            val result = validator.validate(cliente)

            assertTrue(result.isErr)

            assertEquals("El nombre no puede estar en blanco", result.error.message)
        }

        @Test
        @DisplayName("Email con formato incorrecto debe fallar")
        fun emailFormatoInvalido() {
            val cliente = Cliente(
                id = 1,
                nombre = "Juan",
                email = "email",
                password = "123456"
            )

            val result = validator.validate(cliente)

            assertTrue(result.isErr)
            assertEquals("El email debe seguir el formato indicado : example@example.com", result.error.message)
        }

        @Test
        @DisplayName("Password en blanco debe fallar")
        fun passwordEnBlanco() {
            val cliente = Cliente(
                id = 1,
                nombre = "Juan",
                email = "juan@kkarra.com",
                password = ""
            )
            val result = validator.validate(cliente)

            assertTrue(result.isErr)

            assertEquals("La contraseña no puede estar en blanco", result.error.message)
        }

        @Test
        @DisplayName("Password con menos de 6 caracteres debe fallar")
        fun passwordMuyCorta() {

            val cliente = Cliente(
                id = 1,
                nombre = "Juan",
                email = "juan@kkarra.com",
                password = "123"
            )

            val result = validator.validate(cliente)

            assertTrue(result.isErr)
            assertEquals("La contraseña no puede tener menos de 6 caracteres", result.error.message)
        }
    }
}
