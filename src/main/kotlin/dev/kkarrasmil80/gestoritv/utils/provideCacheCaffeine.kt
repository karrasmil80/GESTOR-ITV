package dev.kkarrasmil80.gestoritv.utils

import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.Caffeine
import dev.kkarrasmil80.gestoritv.vehiculo.models.Vehiculo
import org.lighthousegames.logging.logging
import java.util.concurrent.TimeUnit

fun provideCacheCaffeine(): Cache<Int, Vehiculo> {
    val logger = logging()
    logger.debug { "Proporcionando cache caffeine" }
    return Caffeine.newBuilder()
        .maximumSize(10)
        .expireAfterWrite(60, TimeUnit.SECONDS)
        .build<Int, Vehiculo>()
}