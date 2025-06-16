package dev.kkarrasmil80.gestoritv.utils

import dev.kkarrasmil80.gestoritv.vehiculo.dao.VehiculoDao
import org.jdbi.v3.core.Jdbi
import org.lighthousegames.logging.logging

fun provideVehiculoDao(jdbi: Jdbi): VehiculoDao {
    val logger = logging()
    logger.debug { "Inicializando PersonaDao" }
    return jdbi.onDemand(VehiculoDao::class.java)
}