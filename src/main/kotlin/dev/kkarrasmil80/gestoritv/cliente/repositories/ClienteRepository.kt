package dev.kkarrasmil80.gestoritv.cliente.repositories

import dev.kkarrasmil80.gestoritv.cliente.models.Cliente

interface ClienteRepository {

    // Busca un cliente por su email, devuelve null si no existe
    fun findByEmail(email: String): Cliente?

    // Intenta hacer login con email y password, devuelve el cliente si es válido o null si no
    fun login(email: String, password: String): Cliente?
}
