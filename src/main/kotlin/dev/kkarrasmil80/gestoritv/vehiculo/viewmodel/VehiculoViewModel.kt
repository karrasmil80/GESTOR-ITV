package dev.kkarrasmil80.gestoritv.vehiculo.viewmodel

import com.github.michaelbull.result.onSuccess
import dev.kkarrasmil80.gestoritv.vehiculo.error.VehiculoError
import dev.kkarrasmil80.gestoritv.vehiculo.models.Vehiculo
import dev.kkarrasmil80.gestoritv.vehiculo.service.VehiculoServiceImpl
import javafx.beans.property.SimpleObjectProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import org.lighthousegames.logging.logging
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.map
import dev.kkarrasmil80.gestoritv.vehiculo.storage.VehiculoStorageImpl
//import dev.kkarrasmil80.gestoritv.vehiculo.storage.VehiculoStorageZipImpl
import java.io.File

private val logger = logging()

class VehiculoViewModel(
    private val service: VehiculoServiceImpl,
    private val storage: VehiculoStorageImpl,
) {

    // Lista interna de vehículos
    private var vehiculoList: List<Vehiculo> = emptyList()

    // Estado observable para UI
    var state: SimpleObjectProperty<State> = SimpleObjectProperty(State())

    // Estado con lista vacía (no se usa mucho aquí)
    private var vehiculosVacio: List<Vehiculo> = emptyList()

    // Datos observables y estado actual del vehículo
    data class State(
        val observableVehiculo: ObservableList<Vehiculo> = FXCollections.observableArrayList(),
        val vehiculo: VehiculoState = VehiculoState(),
    )

    // Representación del estado del vehículo en UI
    data class VehiculoState(
        val id: Int = 0,
        val matricula: String = "",
        val marca: String = "",
        val modelo: String = "",
        val anio: Int = 0,
        val tipo: String = "",
        val consumo: String? = "",
        val cilindrada: Int? = 0,
        val capacidad: Int? = 0,
        val neumaticos: Boolean = false,
        val bateria: Boolean = false,
        val frenos: Boolean = false,
        val aceite: Int? = 0
    )

    // Obtiene todos los vehículos y los pone en el estado observable
    fun findAllVehiculos() {
        logger.debug { "Obteniendo todos los vehículos de la lista" }
        val findAll = service.findAll().value
        state.value.observableVehiculo.setAll(findAll)
    }

    // Guarda un vehículo y actualiza la lista en caso de éxito
    fun saveVehiculo(vehiculo: Vehiculo): Result<Int, VehiculoError> {
        logger.debug { "Guardando nuevo vehículo: $vehiculo" }
        return service.save(vehiculo).onSuccess {
            state.value.observableVehiculo.add(vehiculo)
        }
    }

    // Elimina un vehículo y lo quita de la lista si se elimina con éxito
    fun deleteVehiculo(vehiculo: Vehiculo): Result<Int, VehiculoError> {
        logger.debug { "Eliminando vehículo de la lista: $vehiculo" }
        val delete = service.deleteById(vehiculo.id).onSuccess {
            state.value.observableVehiculo.removeIf { it.id == vehiculo.id }
        }
        return delete
    }

    // Importa vehículos desde un archivo y actualiiza la lista del servicio
    fun importFromFile(file: File): Result<List<Vehiculo>, VehiculoError> {
        return storage.readFromFile(file).also {
            service.findAll()
        }
    }

    // Exporta vehículos a un archivo
    fun exportToFile(file: File, vehiculos: List<Vehiculo>): Result<Unit, VehiculoError> {
        return storage.writeToFile(file, vehiculos).map { Unit }
    }

    // Filtra la lista observable por marca, modelo y tipo (filtros opccionales)
    fun filteredListVehiculos(modelo: String, marca: String, tipo: String): List<Vehiculo> {
        logger.debug { "Filtrando vehículos" }
        return state.value.observableVehiculo
            .filter { it.marca.contains(marca, ignoreCase = true) || marca.isBlank() }
            .filter { it.modelo.contains(modelo, ignoreCase = true) || modelo.isBlank() }
            .filter { it.tipo.contains(tipo, ignoreCase = true) || tipo.isBlank() }
    }
}
