package dev.kkarrasmil80.gestoritv.cliente.dao

// Entidad que representa un cliente en la base de datos
class ClienteEntity(
    val id: Int,
    val nombre: String,
    val email: String,
    val password: String
)
