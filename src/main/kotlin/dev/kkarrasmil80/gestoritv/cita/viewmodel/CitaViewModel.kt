package dev.kkarrasmil80.gestoritv.cita.viewmodel

import com.github.michaelbull.result.onSuccess
import dev.kkarrasmil80.gestoritv.cita.error.CitaError
import dev.kkarrasmil80.gestoritv.cita.models.Cita
import dev.kkarrasmil80.gestoritv.cita.service.CitaServiceImpl
import dev.kkarrasmil80.gestoritv.vehiculo.models.Vehiculo
import javafx.beans.property.SimpleObjectProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import org.lighthousegames.logging.logging
import com.github.michaelbull.result.Result

private val logger = logging()

class CitaViewModel(
    private val service: CitaServiceImpl,
) {

    var state: SimpleObjectProperty<State> = SimpleObjectProperty(State())

    data class State(
        val observableCitas: ObservableList<Cita> = FXCollections.observableArrayList(),
        val cita: CitaState = CitaState(),
    )

    data class CitaState(
        val id: Int = 0,
        val fechaCita: String = "",
        val hora: String = "",
        val vehiculo: Vehiculo? = null
    )

    // Obtiene todas las citas y actualiza la lista observable
    fun findAllCitas() {
        logger.debug { "Obteniendo todas las citas de la lista" }
        val citas = service.getAll().value
        state.value.observableCitas.setAll(citas)
    }

    // Guarda una cita (inserta o actualiza) y actualiza la lista observable
    fun saveCita(cita: Cita): Result<Int, CitaError> {
        logger.debug { "Guardando cita: $cita" }
        return service.insert(cita).onSuccess { idGuardado ->
            val index = state.value.observableCitas.indexOfFirst { it.id == cita.id }
            val citaConId = cita.copy(id = idGuardado)
            if (index >= 0) {
                state.value.observableCitas[index] = citaConId
            } else {
                state.value.observableCitas.add(citaConId)
            }
        }
    }

    // Elimina una cita por id y actualiza la lista observable
    fun deleteCita(cita: Cita): Result<Int, CitaError> {
        logger.debug { "Eliminando cita: $cita" }
        return service.deleteById(cita.id).onSuccess { deletedCount ->
            state.value.observableCitas.removeIf { it.id == cita.id }
        }
    }

}
