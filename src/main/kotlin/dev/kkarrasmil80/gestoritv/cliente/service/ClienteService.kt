package dev.kkarrasmil80.gestoritv.cliente.service

import com.github.michaelbull.result.Result
import dev.kkarrasmil80.gestoritv.cliente.error.ClienteError
import dev.kkarrasmil80.gestoritv.cliente.models.Cliente

interface ClienteService {
    // Intenta hacer login con email y contraseña, devuelve resultado con Cliente o error
    fun loginOk(email: String, password: String): Result<Cliente, ClienteError>

    // Busca un cliente por email, devuelve resultado con Cliente o error
    fun findByEmail(email: String): Result<Cliente, ClienteError>
}
