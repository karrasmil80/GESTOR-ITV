package dev.kkarrasmil80.gestoritv.di

import com.github.benmanes.caffeine.cache.Cache
import dev.kkarrasmil80.gestoritv.cita.repositories.CitasRepositoryImpl
import dev.kkarrasmil80.gestoritv.cita.service.CitaServiceImpl
import dev.kkarrasmil80.gestoritv.cita.utils.provideCitaDao
import dev.kkarrasmil80.gestoritv.cita.validator.CitaValidator
import dev.kkarrasmil80.gestoritv.cita.viewmodel.CitaViewModel
import dev.kkarrasmil80.gestoritv.cliente.utils.provideClienteDao
import dev.kkarrasmil80.gestoritv.config.Config
import dev.kkarrasmil80.gestoritv.vehiculo.models.Vehiculo
import dev.kkarrasmil80.gestoritv.utils.provideCacheCaffeine
import dev.kkarrasmil80.gestoritv.utils.provideDatabaseManager
import dev.kkarrasmil80.gestoritv.vehiculo.repositories.VehiculoRepositoryImpl
import dev.kkarrasmil80.gestoritv.vehiculo.service.VehiculoServiceImpl
import dev.kkarrasmil80.gestoritv.vehiculo.storage.VehiculoStorageCsv
import dev.kkarrasmil80.gestoritv.vehiculo.storage.VehiculoStorageImpl
//import dev.kkarrasmil80.gestoritv.vehiculo.storage.VehiculoStorageZipImpl
import dev.kkarrasmil80.gestoritv.vehiculo.utils.provideVehiculoDao
import dev.kkarrasmil80.gestoritv.vehiculo.validator.VehiculoElectricoValidator
import dev.kkarrasmil80.gestoritv.vehiculo.validator.VehiculoMotorValidator
import dev.kkarrasmil80.gestoritv.vehiculo.validator.VehiculoPublicoValidator
import dev.kkarrasmil80.gestoritv.vehiculo.validator.VehiculoValidator
import dev.kkarrasmil80.gestoritv.vehiculo.viewmodel.VehiculoViewModel
import org.jdbi.v3.core.Jdbi
import org.koin.dsl.module
import org.lighthousegames.logging.logging

private val logger = logging()

/**
 * Módulo de inyección de dependencias para la aplicación usando Koin.
 *
 * Proporciona singletons para configuración, DAO, servicios, repositorios, validadores y caches
 * relacionados con citas, vehículos y clientes.
 */
val appModule = module {

    /**
     * Proporciona un singleton de la configuración de la aplicación [Config].
     */
    try {
        single { Config() }
    } catch (e : Exception) {
        println(e)
        logger.error { "No se ha podido crear un singleton de la configuracion del programa" }
    }

    /**
     * Proporciona un singleton del gestor de base de datos Jdbi.
     */
    try {
        single<Jdbi> { provideDatabaseManager(
            config = get()
        ) }
    } catch (e : Exception) {
        println(e)
        logger.error { "No se ha podido proporcionar un singleton del manager" }
    }

    /**
     * Proporciona un singleton de la cache basada en Caffeine para objetos [Vehiculo].
     */
    try {
        single<Cache<Int, Vehiculo>> { provideCacheCaffeine() }
    } catch (e : Exception) {
        println(e)
        logger.error { "No se ha podido proporcionar la cache caffeine" }
    }

    /**
     * Proporciona un singleton del DAO de vehículos.
     */
    try {
        single { provideVehiculoDao(
            jdbi = get()
        ) }
    } catch (e : Exception) {
        println(e)
        logger.error { "No se ha podido proporcionar el dao del vehiculo" }
    }

    /**
     * Proporciona un singleton del DAO de citas.
     */
    try {
        single { provideCitaDao(
            jdbi = get()
        ) }
    } catch (e : Exception) {
        println(e)
    }

    /**
     * Proporciona un singleton del DAO de clientes.
     */
    try {
        single { provideClienteDao(
            jdbi = get()
        ) }
    } catch (e : Exception) {
        println(e)
    }


    /**
     * Proporciona un singleton del ViewModel de vehículos.
     */
    try {
        single { VehiculoViewModel(
            service = get(),
            storage = get()
        ) }
    } catch (e : Exception) {
        println(e)
    }

    /**
     * Proporciona un singleton del servicio para vehículos.
     */
    try {
        single { VehiculoServiceImpl(
            repository = get(),
            cache = get()
        ) }
    } catch (e : Exception) {
        println(e)
    }

    /**
     * Proporciona un singleton del repositorio de vehículos.
     */
    try {
        single { VehiculoRepositoryImpl(
            dao = get(),
        ) }
    } catch (e : Exception) {
        println(e)
    }

    /**
     * Proporciona un singleton para el almacenamiento de vehículos en formato CSV.
     */
    try {
        single { VehiculoStorageCsv() }
    } catch (e : Exception) {
        println(e)
    }

    /**
     * Proporciona un singleton para el almacenamiento genérico de vehículos.
     */
    try {
        single { VehiculoStorageImpl() }
    } catch (e : Exception) {
        println(e)
    }

    /**
     * Proporciona un singleton del validador general de vehículos.
     */
    try {
        single { VehiculoValidator() }
    } catch (e : Exception) {
        println(e)
    }

    /**
     * Proporciona un singleton del validador específico para vehículos con motor.
     */
    try {
        single { VehiculoMotorValidator() }
    } catch (e : Exception) {
        println(e)
    }

    /**
     * Proporciona un singleton del validador específico para vehículos de uso público.
     */
    try {
        single { VehiculoPublicoValidator() }
    } catch (e : Exception) {
        println(e)
    }

    /**
     * Proporciona un singleton del validador específico para vehículos eléctricos.
     */
    try {
        single { VehiculoElectricoValidator() }
    } catch (e : Exception) {
        println(e)
    }

    /**
     * Proporciona un singleton del ViewModel para citas.
     */
    try {
        single { CitaViewModel(
            service = get(),
        ) }
    } catch (e : Exception) {
        println(e)
    }

    /**
     * Proporciona un singleton del servicio para citas.
     */
    try {
        single { CitaServiceImpl(
            repositoryVehiculo = get(),
            validador = get(),
            repository = get(),
        ) }
    } catch (e : Exception) {
        println(e)
    }

    /**
     * Proporciona un singleton del validador para citas.
     */
    try {
        single { CitaValidator() }
    } catch (e : Exception) {
        println(e)
    }

    /**
     * Proporciona un singleton del repositorio para citas.
     */
    try {
        single { CitasRepositoryImpl(
            citaDao = get(),
            vehiculoDao = get()
        ) }
    } catch (e : Exception) {
        println(e)
    }
}
