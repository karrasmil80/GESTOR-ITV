package dev.kkarrasmil80.gestoritv.utils

import dev.kkarrasmil80.gestoritv.config.Config
import dev.kkarrasmil80.gestoritv.database.JdbiManager
import org.jdbi.v3.core.Jdbi
import org.lighthousegames.logging.logging

/**
 * Proporciona una instancia de [Jdbi] configurada para la gestión de la base de datos.
 *
 */
fun provideDatabaseManager(config: Config): Jdbi {
    val logger = logging()
    logger.debug { "Proporcionando instancia de JdbiManager" }
    return JdbiManager(
        config.databaseUrl,
        config.databaseInitData,
        config.databaseInitTables,
        config.databaseLogger
    ).jdbi
}