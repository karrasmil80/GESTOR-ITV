package dev.kkarrasmil80.gestoritv.cliente.service

import com.github.michaelbull.result.Result
import dev.kkarrasmil80.gestoritv.cliente.error.ClienteError
import dev.kkarrasmil80.gestoritv.cliente.models.Cliente

interface ClienteService {
    fun loginOk(email: String, password: String): Result<Cliente, ClienteError>
    fun findByEmail(email: String): Result<Cliente, ClienteError>
}