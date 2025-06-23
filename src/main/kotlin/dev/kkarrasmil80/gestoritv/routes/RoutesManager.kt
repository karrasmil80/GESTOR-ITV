package dev.kkarrasmil80.gestoritv.routes

import javafx.application.Application
import javafx.application.Platform
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.control.Alert
import javafx.scene.control.ButtonType
import javafx.scene.layout.Pane
import javafx.stage.Modality
import javafx.stage.Stage
import javafx.stage.WindowEvent
import org.lighthousegames.logging.logging
import java.io.InputStream
import java.net.URL

private val logger = logging()

/**
 * Objeto singleton que administra las rutas y ventanas de la aplicación.
 * Contiene métodos para inicializar y mostrar distintas vistas definidas por fxml,
 * así como controlar la ventana activa y la ventana principal.
 */
object RoutesManager {


    lateinit var escenaPrincipal : Stage

    private lateinit var _escenarioActivo : Stage

    var escenaActiva: Stage
        get() = _escenarioActivo
        set(value) {
            _escenarioActivo = value
        }

    lateinit var app : Application

    private var sceneMap : HashMap<String, Pane> = HashMap()

    /**
     * Enum que representa las distintas vistas de la aplicación y su ruta al archivo FXML correspondiente.
     */
    enum class View(val fxmlPath : String) {
        CITA("view/cita-view.fxml"),
        LOGIN("view/login-view.fxml"),
        MAIN("view/main-view.fxml"),
        SPLASH("view/splash-screen.fxml"),
        HELP("view/help-view.fxml"),
    }

    init {
        logger.debug { "Iniciando Routes Manager" }
    }

    /**
     * Obtiene el recurso URL del archivo FXML a partir de la ruta relativa.
     *
     * @param resource Ruta relativa al recurso.
     * @return URL del recurso.
     * @throws RuntimeException si el recurso no se encuentra.
     */
    fun getResource(resource : String) : URL {
        return app::class.java.getResource(resource)
            ?: throw RuntimeException("Resource $resource no encontrado")
    }

    /**
     * Inicializa la ventana de login con la escena correspondiente.
     *
     * @param mainStage Escenario principal donde se cargará la vista de login.
     */
    fun initLoginStage(mainStage: Stage) {
        logger.debug { "Iniciando ventana de login" }
        val fxmlLoader = FXMLLoader(getResource(View.LOGIN.fxmlPath))
        val root = fxmlLoader.load<Pane>()
        val newScene = Scene(root, 550.0, 400.0)
        mainStage.apply {
            title = "Login ITV"
            scene = newScene
            centerOnScreen()
            isResizable = false
        }.show()

        escenaPrincipal = mainStage
        _escenarioActivo = mainStage
    }

    /**
     * Inicializa y muestra la ventana principal de la aplicación con la escena principal.
     */
    fun initMainViewStage() {
        logger.debug { "Cargando escena new team" }
        val fxmlLoader = FXMLLoader(getResource(View.MAIN.fxmlPath))
        val root = fxmlLoader.load<Pane>()
        val newScene = Scene(root, 685.0, 470.0)

        val newStage = Stage().apply {
            title = "Gestor principal ITV"
            scene = newScene
            centerOnScreen()
            isResizable = false
            show()
        }

        _escenarioActivo = newStage
    }

    /**
     * Inicializa y muestra la ventana "Acerca de" de la aplicación como una ventana modal.
     */
    fun initAcercaDe() {
        logger.debug { "Cargando acerca de" }
        val fxmlLoader = FXMLLoader(getResource(View.HELP.fxmlPath))
        val root = fxmlLoader.load<Pane>()
        val newScene = Scene(root, 600.0, 400.0)
        Stage().apply {
            title = "ITV acerca de"
            scene = newScene
            centerOnScreen()
            isResizable = false
            initModality(Modality.WINDOW_MODAL)
        }.show()
    }

    /**
     * Inicializa y muestra la pantalla de carga (Splash Screen).
     *
     * @param stage Escenario donde se mostrará la pantalla de carga.
     */
    fun initSplashScreen(stage: Stage) {
        logger.debug { "Iniciando Splash screen" }
        val fxmlLoader = FXMLLoader(getResource(View.SPLASH.fxmlPath))
        val root = fxmlLoader.load<Pane>()
        val newScene = Scene(root, 600.0, 380.0)

        escenaPrincipal = stage
        _escenarioActivo = stage

        stage.apply {
            title = "ITV Splash Screen"
            scene = newScene
            centerOnScreen()
            isResizable = false
        }.show()
    }

    /**
     * Inicializa y muestra la ventana de citas en la ventana principal.
     */
    fun initCitaScreen() {
        logger.debug { "Iniciando Panel de Citas en ventana principal" }
        val fxmlLoader = FXMLLoader(getResource(View.CITA.fxmlPath))
        val root = fxmlLoader.load<Pane>()
        val newScene = Scene(root, 320.0, 570.0)
        escenaPrincipal.apply {
            scene = newScene
            centerOnScreen()
            isResizable = false
            show()
        }
        _escenarioActivo = escenaPrincipal
    }

    /**
     * Método para gestionar el evento de salida de la aplicación.
     * Muestra un cuadro de diálogo de confirmación y cierra la aplicación si el usuario acepta.
     *
     * @param title Título del cuadro de diálogo.
     * @param header Texto del encabezado del cuadro de diálogo.
     * @param content Texto del contenido explicativo del cuadro de diálogo.
     * @param event Evento de ventana que puede ser consumido para cancelar el cierre.
     */
    fun onAppExit(
        title : String = "¿Salir de la Aplicacion de New-Team?",
        header : String = "¿Seguro que desea salir de la aplicación?",
        content : String = "Se perderán los datos no guardados",
        event: WindowEvent? = null
    ){
        logger.debug { "Cerrando aplicacion" }
        Alert(Alert.AlertType.CONFIRMATION).apply {
            this.title = title
            this.headerText = header
            this.contentText = content
        }.showAndWait().ifPresent { opcion ->
            if (opcion == ButtonType.OK) {
                Platform.exit()
            } else {
                event?.consume()
            }
        }
    }
}
