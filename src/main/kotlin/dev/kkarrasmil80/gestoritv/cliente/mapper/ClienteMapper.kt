package dev.kkarrasmil80.gestoritv.cliente.mapper

import dev.kkarrasmil80.gestoritv.cliente.dao.ClienteEntity
import dev.kkarrasmil80.gestoritv.cliente.models.Cliente

// Convierte ClienteEntity a Cliente (modelo de dominio)
fun ClienteEntity.toModel(): Cliente {
    return Cliente(
        id = id,
        nombre = nombre,
        email = email,
        password = password
    )
}

// Convierte Cliente (modelo de dominio) a ClienteEntity para BD
fun Cliente.toEntity(): ClienteEntity {
    return ClienteEntity(
        id = id,
        nombre = nombre,
        email = email,
        password = password
    )
}
