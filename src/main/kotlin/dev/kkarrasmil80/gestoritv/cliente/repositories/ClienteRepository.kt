package dev.kkarrasmil80.gestoritv.cliente.repositories

import dev.kkarrasmil80.gestoritv.cliente.models.Cliente

interface ClienteRepository {
    fun findByEmail(email: String) : Cliente?
    fun login(email: String, password: String) : Cliente?
}