package dev.kkarrasmil80.gestoritv.vehiculo.utils

import dev.kkarrasmil80.gestoritv.vehiculo.dao.VehiculoDao
import org.jdbi.v3.core.Jdbi
import org.lighthousegames.logging.logging

// Función para proveer una instancia del DAO de Vehiculo usando Jdbi
fun provideVehiculoDao(jdbi: Jdbi): VehiculoDao {
    val logger = logging() // Crear logger para registrar mensajes
    logger.debug { "Inicializando VehiculoDao" } // Mensaje de depuración sobre la inicialización
    return jdbi.onDemand(VehiculoDao::class.java) // Crear instancia del DAO bajo demanda
}
