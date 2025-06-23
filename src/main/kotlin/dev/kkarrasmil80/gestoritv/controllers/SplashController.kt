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
     * Método que JavaFX llama automáticamente al crear el controlador asociado al FXML.
     * Aquí se inicia la carga simulada en un hilo aparte y se configuran los eventos.
     */
    fun initialize() {
        // Ejecutar la simulación de carga en un hilo separado para no bloquear la UI
        thread { isFinished = carga() }
        // Inicializar eventos, como escuchar cambios en la barra de progreso
        initEvents()
    }

    /**
     * Configura los listeners para los eventos de la barra de progreso.
     * Cuando la barra llegue a 100% (1.0), se lanza la ventana de login y se cierra el splash.
     */
    private fun initEvents(){
        progressB.progressProperty().addListener { _, _, newValue ->
            // Cuando la barra de progreso llega a 1.0 (completada)
            if (newValue == 1.0)
            // Abrir ventana de login pasando la ventana actual (Splash) para cerrarla
                RoutesManager.initLoginStage(progressB.scene.window as Stage)
        }
    }

    /**
     * Simula la carga mostrando el progreso en la barra de progreso.
     * Usa Platform.runLater para actualizar la UI desde el hilo secundario.
     * @return true cuando la carga termina.
     */
    private fun carga(): Boolean {
        // Inicializar la barra en 0%
        progressB.progress = 0.0
        // Ciclo para simular progreso de 0 a 100%
        for (i in 0..100){
            val progress = i / 100.0
            // Actualizar la barra en el hilo principal (JavaFX UI Thread)
            Platform.runLater {
                progressB.progress = progress
            }
            // Esperar 50ms para simular tiempo de carga
            Thread.sleep(50)
        }
        // Cuando termina, retorna true
        return true
    }
}
