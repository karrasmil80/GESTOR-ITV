package dev.kkarrasmil80.gestoritv.cliente.validator

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import dev.kkarrasmil80.gestoritv.cliente.error.ClienteError
import dev.kkarrasmil80.gestoritv.cliente.models.Cliente
import dev.kkarrasmil80.gestoritv.vehiculo.validator.Validator

class ClienteValidator : Validator<Cliente, ClienteError> {
    override fun validate(t: Cliente): Result<Cliente, ClienteError> {
        if (t.nombre.isBlank()) {
            return Err(ClienteError.ClienteValidationError("El nombre no puede estar en blanco"))
        }

        if (!Regex("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$").matches(t.email)) {
            return Err(ClienteError.ClienteValidationError("El email debe seguir el formato indicado : example@example.com"))
        }

        if (t.password.isBlank()) {
            return Err(ClienteError.ClienteValidationError("La contraseña no puede estar en blanco"))
        }

        if (t.password.length < 6) {
            return Err(ClienteError.ClienteValidationError("La contraseña no puede tener menos de 6 caracteres"))
        }

        return Ok(t)
    }
}