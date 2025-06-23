package dev.kkarrasmil80.gestoritv.utils

import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.Caffeine
import dev.kkarrasmil80.gestoritv.vehiculo.models.Vehiculo
import org.lighthousegames.logging.logging
import java.util.concurrent.TimeUnit

/**
 * Proporciona una instancia de caché basada en Caffeine para almacenar objetos [Vehiculo].
 *
 * La caché tiene una capacidad máxima de 10 elementos y expira 60 segundos después
 * de ser escritas.
 *
 * @return Cache<Int, Vehiculo> instancia de caché configurada para vehículos.
 */
fun provideCacheCaffeine(): Cache<Int, Vehiculo> {
    val logger = logging()
    logger.debug { "Proporcionando cache caffeine" }
    return Caffeine.newBuilder()
        .maximumSize(10)
        .expireAfterWrite(60, TimeUnit.SECONDS)
        .build<Int, Vehiculo>()
}