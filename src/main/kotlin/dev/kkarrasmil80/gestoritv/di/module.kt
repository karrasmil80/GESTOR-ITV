package dev.kkarrasmil80.gestoritv.di

import com.github.benmanes.caffeine.cache.Cache
import dev.kkarrasmil80.gestoritv.config.Config
import dev.kkarrasmil80.gestoritv.database.JdbiManager
import dev.kkarrasmil80.gestoritv.vehiculo.models.Vehiculo
import dev.kkarrasmil80.gestoritv.utils.provideCacheCaffeine
import dev.kkarrasmil80.gestoritv.utils.provideDatabaseManager
import dev.kkarrasmil80.gestoritv.utils.provideVehiculoDao
import org.jdbi.v3.core.Jdbi
import org.koin.dsl.module
import org.lighthousegames.logging.logging

private val logger = logging()

val appModule = module {

    /**
     * Crea un singleton de [Config]
     */

    try {
        single { Config() }
    } catch (e : Exception) {
        println(e)
        logger.error { "No se ha podido crear un singleton de la configuracion del programa" }
    }

    /**
     * Proporciona el [JdbiManager]
     */

    try {
        single<Jdbi> { provideDatabaseManager(
            config = get()
        ) }
    } catch (e : Exception) {
        println(e)
        logger.error { "No se ha podido proporcionar un singleton del manager}" }
    }

    /**
     * Proporciona la cache [Caffeine]
     */

    try {
        single<Cache<Int, Vehiculo>> { provideCacheCaffeine() }
    } catch (e : Exception) {
        println(e)
        logger.error { "No se ha podido proporcionar la cache caffeine" }
    }


    /**
     *
     */

    try {
        single { provideVehiculoDao(
            jdbi = get()
        ) }
    } catch (e : Exception) {
        println(e)
        logger.error { "No se ha podido proporcionar el dao del vehiculo" }
    }

}