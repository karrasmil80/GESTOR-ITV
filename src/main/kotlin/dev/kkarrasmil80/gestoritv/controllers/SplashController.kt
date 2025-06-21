package dev.kkarrasmil80.gestoritv.controllers

import dev.kkarrasmil80.gestoritv.routes.RoutesManager
import javafx.application.Platform
import javafx.fxml.FXML
import javafx.scene.control.ProgressBar
import javafx.stage.Stage
import kotlin.concurrent.thread

class SplashController {

    @FXML
    lateinit var progressB: ProgressBar

    private var isFinished = false
    /**
     * Método automáticamente llamado por JavaFX cuando se crea el [SplashController] asociado al correspondiente .fxml
     * @see initEvents
     */
    fun initialize() {
        thread { isFinished = carga() }
        initEvents()
    }

    private fun initEvents(){
        progressB.progressProperty().addListener { _, _, newValue ->
            if (newValue == 1.0) RoutesManager.initLoginStage(progressB.scene.window as Stage)
        }
    }

    private fun carga(): Boolean {
        progressB.progress = 0.0
        for (i in 0..100){
            val progress = i / 100.0
            Platform.runLater {
                progressB.progress = progress

            }
            Thread.sleep(50)
        }
        return true
    }
}