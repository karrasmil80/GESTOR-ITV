package dev.kkarrasmil80.gestoritv.cita.utils

import dev.kkarrasmil80.gestoritv.cita.dao.CitaDao
import dev.kkarrasmil80.gestoritv.vehiculo.dao.VehiculoDao
import org.jdbi.v3.core.Jdbi
import org.lighthousegames.logging.logging

fun provideCitaDao(jdbi: Jdbi): CitaDao {
    val logger = logging()
    logger.debug { "Inicializando CitaDao" }
    return jdbi.onDemand(CitaDao::class.java)
}