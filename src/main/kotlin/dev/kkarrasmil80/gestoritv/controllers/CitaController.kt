package dev.kkarrasmil80.gestoritv.controllers

import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import dev.kkarrasmil80.gestoritv.cita.models.Cita
import dev.kkarrasmil80.gestoritv.cita.viewmodel.CitaViewModel
import dev.kkarrasmil80.gestoritv.vehiculo.models.VehiculoMotor
import javafx.beans.property.SimpleStringProperty
import javafx.fxml.FXML
import javafx.scene.control.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.lighthousegames.logging.logging

private val logger = logging()

class CitaController : KoinComponent {

    // Inyección del ViewModel que maneja la lógica y datos de citas
    private val viewModel: CitaViewModel by inject()


    @FXML
    private lateinit var citaTableView: TableView<Cita>

    @FXML
    private lateinit var idColumn: TableColumn<Cita, String>

    @FXML
    private lateinit var fechaColumn: TableColumn<Cita, String>

    @FXML
    private lateinit var horaColumn: TableColumn<Cita, String>
    @FXML
    private lateinit var vehiculoColumn: TableColumn<Cita, String>

    @FXML
    private lateinit var añadirButton: Button

    @FXML
    private lateinit var eliminarButton: Button
    @FXML
    private lateinit var editarButton: Button

    @FXML
    fun initialize() {
        // Configuramos cada columna para mostrar la propiedad correcta de Cita
        idColumn.setCellValueFactory { SimpleStringProperty(it.value.id.toString()) }
        fechaColumn.setCellValueFactory { SimpleStringProperty(it.value.fechaCita) }
        horaColumn.setCellValueFactory { SimpleStringProperty(it.value.hora) }
        vehiculoColumn.setCellValueFactory {

            SimpleStringProperty(it.value.vehiculo?.toString() ?: "Sin vehículo")
        }

        // Asignamos la lista observable de citas al TableView
        citaTableView.items = viewModel.state.value.observableCitas

        viewModel.findAllCitas()

        // Configurar eventos para los botones
        añadirButton.setOnAction { abrirFormularioNuevaCita() }
        editarButton.setOnAction { editarCitaSeleccionada() }
        eliminarButton.setOnAction { eliminarCitaSeleccionada() }
    }

    // Función para abrir formulario y crear una nueva cita
    private fun abrirFormularioNuevaCita() {
        logger.debug { "Abrir formulario para nueva cita" }

        // Solicitar al usuario la fecha y hora de la cita
        val fecha = solicitarInput("Nueva cita", "Fecha de la cita (yyyy-mm-dd):") ?: return
        val hora = solicitarInput("Nueva cita", "Hora de la cita (HH:mm):") ?: return

        // Creamos un vehículo ejemplo para asociar a la cita
        val vehiculo = VehiculoMotor(
            id = 998,
            matricula = "1234ABC",
            modelo = "Ibiza",
            marca = "SEAT",
            anio = 2020,
            tipo = "Turismo",
            cilindrada = 20,
            aceite = 30,
            neumaticos = true,
            bateria = true,
            frenos = true
        )

        // Creamos la nueva cita (invntada) con los datos ingresados
        val nuevaCita = Cita(
            id = 0,
            fechaCita = fecha,
            hora = hora,
            vehiculo = vehiculo
        )

        viewModel.saveCita(nuevaCita)
            .onSuccess {
                mostrarAlerta("Cita creada con id $it")
                viewModel.findAllCitas()
            }
            .onFailure {
                mostrarAlerta("Error creando cita: ${it.message}")
            }
    }

    // Función para editar la cita seleccionada en la tabla
    private fun editarCitaSeleccionada() {
        val citaSeleccionada = citaTableView.selectionModel.selectedItem
        if (citaSeleccionada == null) {
            mostrarAlerta("Selecciona una cita para editar")
            return
        }
        logger.debug { "Editar cita: $citaSeleccionada" }

        // Pedir nuevas fecha y hora, con valores por defecto actuales
        val nuevaFecha = solicitarInput("Editar cita", "Fecha actual: ${citaSeleccionada.fechaCita}. Nueva fecha:", citaSeleccionada.fechaCita) ?: return
        val nuevaHora = solicitarInput("Editar cita", "Hora actual: ${citaSeleccionada.hora}. Nueva hora:", citaSeleccionada.hora) ?: return
        val vehiculo = citaSeleccionada.vehiculo // Conservamos el vehículo

        val citaEditada = citaSeleccionada.copy(
            fechaCita = nuevaFecha,
            hora = nuevaHora,
            vehiculo = vehiculo
        )

        viewModel.saveCita(citaEditada)
            .onSuccess {
                mostrarAlerta("Cita editada correctamente")
                viewModel.findAllCitas()
            }
            .onFailure {
                mostrarAlerta("Error editando cita: ${it.message}")
            }
    }

    // Función para eliminar la cita seleccionada
    private fun eliminarCitaSeleccionada() {
        val citaSeleccionada = citaTableView.selectionModel.selectedItem
        if (citaSeleccionada == null) {
            mostrarAlerta("Selecciona una cita para eliminar")
            return
        }
        logger.debug { "Eliminar cita: $citaSeleccionada" }

        viewModel.deleteCita(citaSeleccionada)
            .onSuccess {
                mostrarAlerta("Cita eliminada correctamente")
                viewModel.findAllCitas()
            }
            .onFailure {
                mostrarAlerta("Error eliminando cita: ${it.message}")
            }
    }

    private fun mostrarAlerta(mensaje: String) {
        val alerta = Alert(Alert.AlertType.INFORMATION)
        alerta.title = "Información"
        alerta.headerText = null
        alerta.contentText = mensaje
        alerta.showAndWait()
    }

    // Función para pedir texto al usuario con un cuadro de diálogo
    private fun solicitarInput(title: String, content: String, defaultValue: String = ""): String? {
        val dialog = TextInputDialog(defaultValue)
        dialog.title = title
        dialog.headerText = null
        dialog.contentText = content

        val result = dialog.showAndWait()
        // Retorna el texto si no está vacío, sino null
        return if (result.isPresent && result.get().isNotBlank()) {
            result.get()
        } else null
    }
}
