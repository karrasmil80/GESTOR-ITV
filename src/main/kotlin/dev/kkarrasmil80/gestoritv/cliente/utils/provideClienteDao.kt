package dev.kkarrasmil80.gestoritv.cliente.utils

import dev.kkarrasmil80.gestoritv.cita.dao.CitaDao
import dev.kkarrasmil80.gestoritv.cliente.dao.ClienteDao
import org.jdbi.v3.core.Jdbi
import org.lighthousegames.logging.logging

fun provideClienteDao(jdbi: Jdbi): ClienteDao {
    val logger = logging()
    logger.debug { "Inicializando ClienteDao" }
    return jdbi.onDemand(ClienteDao::class.java)
}