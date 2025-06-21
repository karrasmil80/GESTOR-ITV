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

    enum class View(val fxmlPath : String) {
        CITA("view/cita-view.fxml"),
        LOGIN("view/login-view.fxml"),
        MAIN("view/main-view.fxml"),
        SPLASH("view/splash-screen.fxml"),
        HELP("view/help-view.fxml")
    }

    init {
        logger.debug { "Iniciando Routes Manager" }
    }

    fun getResourceAsStream(resource : String) : InputStream {
        return app::class.java.getResourceAsStream(resource)
            ?:throw RuntimeException("Resource como stream : $resource no encontrado")
    }

    fun getResource(resource : String) : URL {
        return app::class.java.getResource(resource)
            ?: throw RuntimeException("Resource $resource no encontrado")
    }

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

    fun initMainViewStage() {
        logger.debug { "Cargando escena new team" }
        val fxmlLoader = FXMLLoader(getResource(View.MAIN.fxmlPath))
        val root = fxmlLoader.load<Pane>()
        val newScene = Scene(root, 1400.0, 780.0)

        val newStage = Stage().apply {
            title = "Gestor principal ITV"
            scene = newScene
            centerOnScreen()
            isResizable = false
            show()
        }

        _escenarioActivo = newStage
    }

    fun initAcercaDe() {
        logger.debug { "Cargando acerca de" }
        val fxmlLoader = FXMLLoader(getResource(View.HELP.fxmlPath))
        val root = fxmlLoader.load<Pane>()
        val newScene = Scene(root, 1500.0, 500.0)
        Stage().apply {
            title = "ITV acerca de"
            scene = newScene
            centerOnScreen()
            isResizable = false
            initModality(Modality.WINDOW_MODAL)

        }.show()
    }

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

    fun initCitaScreen() {
        logger.debug { "Iniciando Banquillo New Team" }
        val fxmlLoader = FXMLLoader(getResource(View.CITA.fxmlPath))
        val root = fxmlLoader.load<Pane>()
        val newScene = Scene(root, 516.0, 700.0)
        Stage().apply {
            title = "Cita ITV"
            scene = newScene
            centerOnScreen()
            isResizable = false
        }.show()
    }

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