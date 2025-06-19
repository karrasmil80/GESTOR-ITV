package dev.kkarrasmil80.gestoritv.cliente.service

import com.github.michaelbull.result.Err
import com.github.michaelbull.result.Ok
import com.github.michaelbull.result.Result
import dev.kkarrasmil80.gestoritv.cliente.error.ClienteError
import dev.kkarrasmil80.gestoritv.cliente.models.Cliente
import dev.kkarrasmil80.gestoritv.cliente.repositories.ClienteRepository
import dev.kkarrasmil80.gestoritv.cliente.repositories.ClienteRepositoryImpl
import dev.kkarrasmil80.gestoritv.cliente.validator.ClienteValidator

class ClienteServiceImpl(
    private val repository : ClienteRepositoryImpl,
    private val validator : ClienteValidator
) : ClienteService {
    override fun loginOk(email: String, password: String): Result<Cliente, ClienteError> {
        val cliente = repository.findByEmail(email)
            ?: return Err(ClienteError.ClienteServiceError("El email no existe"))

        return if (cliente.password == password) {
            Ok(cliente)
        } else {
            Err(ClienteError.ClienteValidationError("Contraseña incorrecta"))
        }
    }

    override fun findByEmail(email: String): Result<Cliente, ClienteError> {
        return repository.findByEmail(email)?.let {
            Ok(it)
        } ?: Err(ClienteError.ClienteServiceError("El email no existe"))
    }
}