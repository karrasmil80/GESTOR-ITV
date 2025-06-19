package dev.kkarrasmil80.gestoritv.cliente.models

data class Cliente(
    val id: Int,
    val nombre: String,
    val email: String,
    val password: String
) {
}