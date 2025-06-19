package dev.kkarrasmil80.gestoritv.cliente.mapper

import dev.kkarrasmil80.gestoritv.cliente.dao.ClienteEntity
import dev.kkarrasmil80.gestoritv.cliente.models.Cliente

fun ClienteEntity.toModel() : Cliente {
    return Cliente(
        id = id,
        nombre = nombre,
        email = email,
        password = password
    )
}

fun Cliente.toEntity() : ClienteEntity {
    return ClienteEntity(
        id = id,
        nombre = nombre,
        email = email,
        password = password
    )
}