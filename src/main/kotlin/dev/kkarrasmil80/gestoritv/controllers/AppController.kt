package dev.kkarrasmil80.gestoritv.controllers

import com.github.michaelbull.result.onFailure
import com.github.michaelbull.result.onSuccess
import dev.kkarrasmil80.gestoritv.routes.RoutesManager
import dev.kkarrasmil80.gestoritv.vehiculo.error.VehiculoError
import dev.kkarrasmil80.gestoritv.vehiculo.models.Vehiculo
import dev.kkarrasmil80.gestoritv.vehiculo.validator.VehiculoValidator
import dev.kkarrasmil80.gestoritv.vehiculo.viewModel.VehiculoViewModel
import javafx.application.Platform
import javafx.collections.FXCollections
import javafx.collections.transformation.FilteredList
import javafx.fxml.FXML
import javafx.scene.Cursor
import javafx.scene.control.*
import javafx.scene.control.cell.PropertyValueFactory
import javafx.stage.FileChooser
import javafx.stage.Stage
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.lighthousegames.logging.logging
import com.github.michaelbull.result.Result
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder
import dev.kkarrasmil80.gestoritv.vehiculo.models.VehiculoElectrico
import dev.kkarrasmil80.gestoritv.vehiculo.models.VehiculoMotor
import dev.kkarrasmil80.gestoritv.vehiculo.models.VehiculoPublico
import dev.kkarrasmil80.gestoritv.vehiculo.validator.VehiculoElectricoValidator
import dev.kkarrasmil80.gestoritv.vehiculo.validator.VehiculoMotorValidator
import dev.kkarrasmil80.gestoritv.vehiculo.validator.VehiculoPublicoValidator
import java.awt.Desktop
import java.io.File

private val logger = logging()
class AppController : KoinComponent {

    private val validador : VehiculoValidator by inject()
    private val validadorMotor : VehiculoMotorValidator by inject()
    private val validadorPublico : VehiculoPublicoValidator by inject()
    private val validadorElectrico : VehiculoElectricoValidator by inject()
    private val viewModel : VehiculoViewModel by inject()

    private lateinit var vehiculosFilteredList: FilteredList<Vehiculo>


    @FXML
    lateinit var tipoColumn: TableColumn<String, Vehiculo>

    @FXML
    lateinit var EliminarButton: Button

    @FXML
    lateinit var editarButton: Button

    @FXML
    lateinit var añadirButton: Button

    @FXML
    lateinit var validacionButton: Button

    @FXML
    lateinit var panelCitasButton: Button

    @FXML
    lateinit var filterBox: ComboBox<String>

    @FXML
    lateinit var nevagationField: TextField

    @FXML
    lateinit var scrollBar: ScrollBar

    @FXML
    lateinit var capacidadText: TextField

    @FXML
    lateinit var cilindradaText: TextField

    @FXML
    lateinit var consumoText: TextField

    @FXML
    lateinit var tipoText: TextField

    @FXML
    lateinit var añoText: TextField

    @FXML
    lateinit var marcaText: TextField

    @FXML
    lateinit var modeloText: TextField

    @FXML
    lateinit var matriculaText: TextField

    @FXML
    lateinit var idText: TextField

    @FXML
    lateinit var marcaField: TableColumn<String, Vehiculo>

    @FXML
    lateinit var modeloField: TableColumn<String, Vehiculo>

    @FXML
    lateinit var idField: TableColumn<String, Vehiculo>

    @FXML
    lateinit var VehiculoList: TableView<Vehiculo>

    @FXML
    lateinit var ayudaMenu: MenuItem

    @FXML
    lateinit var deleteMenu: MenuItem

    @FXML
    lateinit var EditarMenu: MenuItem

    @FXML
    lateinit var añadirMenu: MenuItem

    @FXML
    lateinit var exportarMenu: MenuItem

    @FXML
    lateinit var importarMenu: MenuItem

    @FXML
    lateinit var closeMenu: MenuItem

    fun initialize() {
        initDefaultValues()
        initBindings()
    }

    fun initBindings() {
        filterBox.items = FXCollections.observableArrayList("Todos", "Marca", "Modelo", "Tipo")
        filterBox.selectionModel.selectFirst()

        // Lista observable y lista filtrada
        val vehiculosObservableList = viewModel.state.value.observableVehiculo
        vehiculosFilteredList = FilteredList(vehiculosObservableList)
        VehiculoList.items = vehiculosFilteredList

        initFilters()
    }

    private fun initFilters() {
        filterBox.selectionModel.selectedItemProperty().addListener { _, _, _ ->
            applyFilters()
        }

        nevagationField.textProperty().addListener { _, _, _ ->
            applyFilters()
        }

    }

    private fun applyFilters() {
        val filtroSeleccionado = filterBox.selectionModel.selectedItem
        val textoBusqueda = nevagationField.text.trim()
        val listaOriginal = viewModel.state.value.observableVehiculo

        val listaFiltrada = if (textoBusqueda.isBlank() || filtroSeleccionado == "Todos") {
            listaOriginal
        } else {
            listaOriginal.filter { vehiculo ->
                when (filtroSeleccionado) {
                    "Marca" -> vehiculo.marca.contains(textoBusqueda, ignoreCase = true)
                    "Modelo" -> vehiculo.modelo.contains(textoBusqueda, ignoreCase = true)
                    "Tipo" -> vehiculo.tipo.contains(textoBusqueda, ignoreCase = true)
                    else -> true
                }
            }
        }

        VehiculoList.items = FXCollections.observableArrayList(listaFiltrada)
    }

    private fun initDefaultValues() {

        idField.cellValueFactory = PropertyValueFactory("id")
        modeloField.cellValueFactory = PropertyValueFactory("modelo")
        marcaField.cellValueFactory = PropertyValueFactory("marca")
        tipoColumn.cellValueFactory = PropertyValueFactory("tipo")

        VehiculoList.selectionModel.selectedItemProperty().addListener { _, _, newValue ->
            if (newValue != null) {
                idText.text = newValue.id.toString()
                matriculaText.text = newValue.matricula.toString()
                modeloText.text = newValue.modelo.toString()
                añoText.text = newValue.anio.toString()
                tipoText.text = newValue.tipo
                marcaText.text = newValue.marca.toString()

            }
        }
    }


    fun onCloseMenuButtonClicked() {
        RoutesManager.onAppExit()
    }

    fun onImportarMenuButtonClicked() {
        val fileChooser = FileChooser().apply {
            title = "Selecciona el archivo a importar"
            extensionFilters.addAll(
                FileChooser.ExtensionFilter("CSV", "*.csv"),
                FileChooser.ExtensionFilter("JSON", "*.json")
            )
        }

        val stage = RoutesManager.escenaActiva.scene.window as Stage
        val selectedFile = fileChooser.showOpenDialog(stage)

        selectedFile?.let { file ->
            RoutesManager.escenaActiva.scene.cursor = Cursor.WAIT

            Platform.runLater {
                viewModel.importFromFile(file)
                    .onSuccess { nuevosVehiculos ->

                        viewModel.state.value.observableVehiculo.setAll(nuevosVehiculos)

                        showAlertOperation(
                            title = "Importación completada",
                            mensaje = "Se han importado ${nuevosVehiculos.size} vehículos correctamente."
                        )
                    }
                    .onFailure { error ->
                        showAlertOperation(
                            alerta = Alert.AlertType.ERROR,
                            title = "Error al importar",
                            mensaje = error.message
                        )
                    }

                RoutesManager.escenaActiva.scene.cursor = Cursor.DEFAULT
            }
        } ?: println("Importación cancelada por el usuario.")
    }


    fun onExportarMenuButtonClicked() {
        val fileChooser = FileChooser().apply {
            extensionFilters.add(FileChooser.ExtensionFilter("JSON", "*.json"))
            extensionFilters.add(FileChooser.ExtensionFilter("CSV", "*.csv"))
            title = "Selecciona dónde guardar el archivo"
            initialFileName = "vehiculos"
        }

        val stage = RoutesManager.escenaActiva.scene.window as Stage
        val selectedFile = fileChooser.showSaveDialog(stage)

        selectedFile?.let { file ->
            RoutesManager.escenaActiva.scene.cursor = Cursor.WAIT

            Platform.runLater {
                viewModel.exportToFile(file, viewModel.state.value.observableVehiculo)
                    .onSuccess {
                        showAlertOperation(
                            title = "Datos exportados",
                            mensaje = "Se han exportado los vehículos correctamente."
                        )
                    }
                    .onFailure { error: VehiculoError ->
                        showAlertOperation(
                            alerta = Alert.AlertType.ERROR,
                            title = "Error al exportar",
                            mensaje = error.message
                        )
                    }

                RoutesManager.escenaActiva.scene.cursor = Cursor.DEFAULT
            }
        } ?: run {
            // Usuario canceló exportación
            println("Exportación cancelada por el usuario.")
        }
    }

    private fun showAlertOperation(
        alerta: Alert.AlertType = Alert.AlertType.CONFIRMATION,
        title: String = "",
        mensaje: String = ""
    ) {
        Alert(alerta).apply {
            this.title = title
            this.contentText = mensaje
        }.showAndWait()
    }

    fun onValidarVehiculo() {
        val vehiculoSeleccionado = VehiculoList.selectionModel.selectedItem

        if (vehiculoSeleccionado == null) {
            showAlertOperation(
                alerta = Alert.AlertType.WARNING,
                title = "Validación",
                mensaje = "Selecciona un vehículo primero."
            )
            return
        }

        try {
            val resultado = when (vehiculoSeleccionado) {
                is VehiculoMotor -> validadorMotor.validate(vehiculoSeleccionado)
                is VehiculoElectrico -> validadorElectrico.validate(vehiculoSeleccionado)
                is VehiculoPublico -> validadorPublico.validate(vehiculoSeleccionado)
                else -> validador.validate(vehiculoSeleccionado)
            }

            resultado
                .onSuccess {
                    showAlertOperation(
                        alerta = Alert.AlertType.INFORMATION,
                        title = "Validación exitosa",
                        mensaje = "El vehículo es válido."
                    )
                }
                .onFailure { errores ->
                    showAlertOperation(
                        alerta = Alert.AlertType.ERROR,
                        title = "Errores de validación",
                        mensaje = errores.message
                    )
                }

        } catch (e: Exception) {
            println(e)
            showAlertOperation(
                alerta = Alert.AlertType.ERROR,
                title = "Error",
                mensaje = "Error inesperado: ${e.message}"
            )
        }
        validarVehiculoYGuardarReporte(
            stage = validacionButton.scene.window as Stage,
            vehiculo = vehiculoSeleccionado
        )
    }

    fun validarVehiculoYGuardarReporte(vehiculo: Vehiculo, stage: Stage) {
        val validaciones = mutableListOf<String>()
        val fallos = mutableListOf<String>()

        when (vehiculo) {
            is VehiculoElectrico -> {
                if (vehiculo.consumo.isBlank()) fallos.add("Consumo eléctrico no especificado")
                if (!vehiculo.bateria) fallos.add("Fallo en batería")
                if (!vehiculo.frenos) fallos.add("Fallo en frenos")
                if (!vehiculo.neumaticos) fallos.add("Fallo en neumáticos")
            }
            is VehiculoMotor -> {
                if (vehiculo.cilindrada <= 0) fallos.add("Cilindrada inválida")
                if (vehiculo.aceite <= 0) fallos.add("Nivel de aceite inválido")
                if (!vehiculo.bateria) fallos.add("Fallo en batería")
                if (!vehiculo.frenos) fallos.add("Fallo en frenos")
                if (!vehiculo.neumaticos) fallos.add("Fallo en neumáticos")
            }
            is VehiculoPublico -> {
                if (vehiculo.capacidad <= 0) fallos.add("Capacidad inválida")
                if (!vehiculo.bateria) fallos.add("Fallo en batería")
                if (!vehiculo.frenos) fallos.add("Fallo en frenos")
                if (!vehiculo.neumaticos) fallos.add("Fallo en neumáticos")
            }
            else -> {
                fallos.add("Tipo de vehículo desconocido")
            }
        }

        if (fallos.isEmpty()) {
            validaciones.add("Todos los chequeos pasaron correctamente.")
        }

        val infoHtml = buildString {
            append("<html xmlns='http://www.w3.org/1999/xhtml'><head>")
            append("<meta http-equiv='Content-Type' content='text/html; charset=UTF-8' />")
            append("<title>Reporte ITV</title></head><body>")
            append("<h1>Reporte del Vehículo</h1>")
            append("<p><strong>Matrícula:</strong> ${vehiculo.matricula}</p>")
            append("<p><strong>Marca:</strong> ${vehiculo.marca}</p>")
            append("<p><strong>Modelo:</strong> ${vehiculo.modelo}</p>")
            append("<p><strong>Año:</strong> ${vehiculo.anio}</p>")
            append("<p><strong>Tipo:</strong> ${vehiculo.tipo}</p>")

            append("<h2>Datos Técnicos</h2><ul>")
            when (vehiculo) {
                is VehiculoElectrico -> {
                    append("<li>Consumo: ${vehiculo.consumo}</li>")
                    append("<li>Neumáticos: ${if (vehiculo.neumaticos) "OK" else "Fallo"}</li>")
                    append("<li>Batería: ${if (vehiculo.bateria) "OK" else "Fallo"}</li>")
                    append("<li>Frenos: ${if (vehiculo.frenos) "OK" else "Fallo"}</li>")
                }
                is VehiculoMotor -> {
                    append("<li>Cilindrada: ${vehiculo.cilindrada} cc</li>")
                    append("<li>Aceite: ${vehiculo.aceite}</li>")
                    append("<li>Neumáticos: ${if (vehiculo.neumaticos) "OK" else "Fallo"}</li>")
                    append("<li>Batería: ${if (vehiculo.bateria) "OK" else "Fallo"}</li>")
                    append("<li>Frenos: ${if (vehiculo.frenos) "OK" else "Fallo"}</li>")
                }
                is VehiculoPublico -> {
                    append("<li>Capacidad: ${vehiculo.capacidad}</li>")
                    append("<li>Neumáticos: ${if (vehiculo.neumaticos) "OK" else "Fallo"}</li>")
                    append("<li>Batería: ${if (vehiculo.bateria) "OK" else "Fallo"}</li>")
                    append("<li>Frenos: ${if (vehiculo.frenos) "OK" else "Fallo"}</li>")
                }
            }
            append("</ul>")

            append("<h2>Resultado de la ITV</h2>")
            if (fallos.isEmpty()) {
                append("<p style='color:green'><strong>Todas las pruebas han sido superadas correctamente.</strong></p>")
            } else {
                append("<p style='color:red'><strong>El vehículo NO ha superado la ITV. Fallos encontrados:</strong></p>")
                append("<ul>")
                fallos.forEach { error -> append("<li>$error</li>") }
                append("</ul>")
            }

            append("</body></html>")
        }

        // Mostrar cuadro de diálogo para guardar como PDF o HTML
        val fileChooser = FileChooser().apply {
            title = "Guardar reporte del vehículo"
            extensionFilters.addAll(
                FileChooser.ExtensionFilter("Archivo PDF", "*.pdf"),
                FileChooser.ExtensionFilter("Archivo HTML", "*.html")
            )
        }

        val file: File? = fileChooser.showSaveDialog(stage)

        file?.let {
            try {
                if (it.extension.lowercase() == "pdf") {
                    exportHtmlToPdf(infoHtml, it)
                } else {
                    it.writeText(infoHtml)
                }
                Desktop.getDesktop().browse(it.toURI())
            } catch (ex: Exception) {
                println("Error al guardar o abrir el archivo: ${ex.message}")
            }
        }
    }

    fun exportHtmlToPdf(html: String, outputFile: File) {
        outputFile.outputStream().use { os ->
            PdfRendererBuilder()
                .useFastMode()
                .withHtmlContent(html, null)
                .toStream(os)
                .run()
        }
    }

    fun onAboutAction() {
        RoutesManager.initAcercaDe()
        AboutController()
    }

    fun eliminarAction() {
        val seleccionado = VehiculoList.selectionModel.selectedItem
        if (seleccionado == null) {
            val alerta = Alert(Alert.AlertType.WARNING)
            alerta.title = "Advertencia"
            alerta.headerText = null
            alerta.contentText = "Por favor, selecciona un vehículo para eliminar."
            alerta.showAndWait()
            return
        }

        val confirmacion = Alert(Alert.AlertType.CONFIRMATION)
        confirmacion.title = "Confirmar eliminación"
        confirmacion.headerText = null
        confirmacion.contentText = "¿Estás seguro de que quieres eliminar el vehículo ${seleccionado.marca} ${seleccionado.modelo}?"

        val respuesta = confirmacion.showAndWait()

        if (respuesta.isPresent && respuesta.get() == ButtonType.OK) {
            viewModel.state.value.observableVehiculo.remove(seleccionado)

            // Mostrar mensaje de éxito
            val info = Alert(Alert.AlertType.INFORMATION)
            info.title = "Eliminado"
            info.headerText = null
            info.contentText = "Vehículo eliminado correctamente."
            info.showAndWait()
        }
    }

    fun citasButtonClick() {
        RoutesManager.initCitaScreen()
    }

    fun añadirVehiculoDialog() {
        val matricula = solicitarInput("Nuevo vehículo", "Matrícula:") ?: return
        val marca = solicitarInput("Nuevo vehículo", "Marca:") ?: return
        val modelo = solicitarInput("Nuevo vehículo", "Modelo:") ?: return
        val anioStr = solicitarInput("Nuevo vehículo", "Año:") ?: return
        val tipo = solicitarInput("Nuevo vehículo", "Tipo:") ?: return

        val anio = anioStr.toIntOrNull()
        if (anio == null) {
            showAlertOperation(Alert.AlertType.ERROR, "Error", "El año debe ser un número válido.")
            return
        }

        val nuevoVehiculo = VehiculoMotor(
            id = 0,
            matricula = matricula,
            marca = marca,
            modelo = modelo,
            anio = anio,
            tipo = tipo,
            cilindrada = 20,
            aceite = 10,
            neumaticos = true,
            bateria = true,
            frenos = true
        )

        Platform.runLater {
            viewModel.state.value.observableVehiculo.add(nuevoVehiculo)
            showAlertOperation(Alert.AlertType.INFORMATION, "Éxito", "Vehículo añadido correctamente.")
        }
    }

    fun solicitarInput(titulo: String, mensaje: String): String? {
        val dialog = TextInputDialog()
        dialog.title = titulo
        dialog.headerText = null
        dialog.contentText = mensaje

        val result = dialog.showAndWait()
        return if (result.isPresent) result.get().trim().takeIf { it.isNotEmpty() } else null
    }
}
