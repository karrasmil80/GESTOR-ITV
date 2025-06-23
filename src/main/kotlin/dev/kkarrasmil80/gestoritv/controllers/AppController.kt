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
import dev.kkarrasmil80.gestoritv.controllers.AboutController
import dev.kkarrasmil80.gestoritv.vehiculo.models.VehiculoElectrico
import dev.kkarrasmil80.gestoritv.vehiculo.models.VehiculoMotor
import dev.kkarrasmil80.gestoritv.vehiculo.models.VehiculoPublico
import dev.kkarrasmil80.gestoritv.vehiculo.validator.VehiculoElectricoValidator
import dev.kkarrasmil80.gestoritv.vehiculo.validator.VehiculoMotorValidator
import dev.kkarrasmil80.gestoritv.vehiculo.validator.VehiculoPublicoValidator
import java.awt.Desktop
import java.io.File

// Controlador principal de la app que maneja la UI y la lógica de vehículos
class AppController : KoinComponent {

    // Validadores y ViewModel que inyectamos con Koin
    private val validador : VehiculoValidator by inject()
    private val validadorMotor : VehiculoMotorValidator by inject()
    private val validadorPublico : VehiculoPublicoValidator by inject()
    private val validadorElectrico : VehiculoElectricoValidator by inject()
    private val viewModel : VehiculoViewModel by inject()

    private lateinit var vehiculosFilteredList: FilteredList<Vehiculo>

    @FXML lateinit var tipoColumn: TableColumn<String, Vehiculo>

    @FXML lateinit var EliminarButton: Button

    @FXML lateinit var editarButton: Button

    @FXML lateinit var añadirButton: Button

    @FXML lateinit var validacionButton: Button

    @FXML lateinit var panelCitasButton: Button

    @FXML lateinit var filterBox: ComboBox<String>

    @FXML lateinit var nevagationField: TextField

    @FXML lateinit var scrollBar: ScrollBar

    @FXML lateinit var capacidadText: TextField

    @FXML lateinit var cilindradaText: TextField

    @FXML lateinit var consumoText: TextField

    @FXML lateinit var tipoText: TextField

    @FXML lateinit var añoText: TextField

    @FXML lateinit var marcaText: TextField

    @FXML lateinit var modeloText: TextField

    @FXML lateinit var matriculaText: TextField

    @FXML lateinit var idText: TextField

    @FXML lateinit var marcaField: TableColumn<String, Vehiculo>

    @FXML lateinit var modeloField: TableColumn<String, Vehiculo>

    @FXML lateinit var idField: TableColumn<String, Vehiculo>

    @FXML lateinit var VehiculoList: TableView<Vehiculo>

    @FXML lateinit var ayudaMenu: MenuItem

    @FXML lateinit var deleteMenu: MenuItem

    @FXML lateinit var EditarMenu: MenuItem

    @FXML lateinit var añadirMenu: MenuItem

    @FXML lateinit var exportarMenu: MenuItem

    @FXML lateinit var importarMenu: MenuItem

    @FXML lateinit var closeMenu: MenuItem

    // Se llama al iniciar la UI, configura valores y enlaces
    fun initialize() {
        initDefaultValues()
        initBindings()
    }

    // Inicializa la lista filtrable y los filtros
    fun initBindings() {
        filterBox.items = FXCollections.observableArrayList("Todos", "Marca", "Modelo", "Tipo")
        filterBox.selectionModel.selectFirst()

        val vehiculosObservableList = viewModel.state.value.observableVehiculo
        vehiculosFilteredList = FilteredList(vehiculosObservableList)
        VehiculoList.items = vehiculosFilteredList

        initFilters()
    }

    // Configura los listeners para aplicar filtros
    private fun initFilters() {
        filterBox.selectionModel.selectedItemProperty().addListener { _, _, _ -> applyFilters() }
        nevagationField.textProperty().addListener { _, _, _ -> applyFilters() }
    }

    // Aplica filtro según selección y texto de búsqueda
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

    // Define cómo se muestran las columnas y actualiza campos al seleccionar vehículo
    private fun initDefaultValues() {
        idField.cellValueFactory = PropertyValueFactory("id")
        modeloField.cellValueFactory = PropertyValueFactory("modelo")
        marcaField.cellValueFactory = PropertyValueFactory("marca")
        tipoColumn.cellValueFactory = PropertyValueFactory("tipo")

        VehiculoList.selectionModel.selectedItemProperty().addListener { _, _, newValue ->
            newValue?.let {
                idText.text = it.id.toString()
                matriculaText.text = it.matricula
                modeloText.text = it.modelo
                añoText.text = it.anio.toString()
                tipoText.text = it.tipo
                marcaText.text = it.marca
            }
        }
    }

    // Cierra la app
    fun onCloseMenuButtonClicked() {
        RoutesManager.onAppExit()
    }

    // Importa vehículos desde archivo CSV o JSON
    fun onImportarMenuButtonClicked() {
        // abre diálogo para elegir archivo y llama a viewModel para importar
    }

    // Exporta vehículos a archivo JSON o CSV
    fun onExportarMenuButtonClicked() {
        // abre diálogo para guardar y llama a viewModel para exportar
    }

    // Muestra alerta con resultado o error
    private fun showAlertOperation(alerta: Alert.AlertType = Alert.AlertType.CONFIRMATION, title: String = "", mensaje: String = "") {
        Alert(alerta).apply {
            this.title = title
            this.contentText = mensaje
        }.showAndWait()
    }

    // Valida el vehículo seleccionado y muestra resultados
    fun onValidarVehiculo() {
        // valida según tipo y muestra mensaje de éxito o error
        // genera reporte y lo guarda/abre
    }

    // Crea y guarda reporte HTML/PDF con estado del vehículo
    fun validarVehiculoYGuardarReporte(vehiculo: Vehiculo, stage: Stage) {
        // genera contenido, abre diálogo para guardar y exporta
    }

    // Convierte HTML a PDF usando PdfRendererBuilder
    fun exportHtmlToPdf(html: String, outputFile: File) {
        // exporta html a pdf
    }

    // Abre ventana "Acerca de"
    fun onAboutAction() {
        RoutesManager.initAcercaDe()
        AboutController()
    }

    // Elimina vehículo seleccionado tras confirmar
    fun eliminarAction() {
        // chequea selección, confirma, elimina y muestra mensaje
    }

    // Abre pantalla de citas
    fun citasButtonClick() {
        RoutesManager.initCitaScreen()
    }

    // Abre diálogo para añadir vehículo nuevo
    fun añadirVehiculoDialog() {
        // pide datos, crea vehículo y lo añade a la lista
    }

    // Muestra diálogo para pedir texto al usuario
    fun solicitarInput(titulo: String, mensaje: String): String? {
        val dialog = TextInputDialog()
        dialog.title = titulo
        dialog.headerText = null
        dialog.contentText = mensaje

        val result = dialog.showAndWait()
        return if (result.isPresent) result.get().trim().takeIf { it.isNotEmpty() } else null
    }

}
