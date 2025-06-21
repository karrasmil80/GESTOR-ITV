package dev.kkarrasmil80.gestoritv

import dev.kkarrasmil80.gestoritv.di.appModule
import dev.kkarrasmil80.gestoritv.routes.RoutesManager
import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.stage.Stage
import org.koin.core.component.KoinComponent
import org.koin.core.context.startKoin
import org.lighthousegames.logging.logging

private val logger = logging()
class HelloApplication : Application(), KoinComponent {

    init {
        println("HelloApplication")
        startKoin {
            printLogger()
            modules(appModule)
        }
    }

    override fun start(stage: Stage) {
        logger.debug { "Iniciando Gestor de ITV" }
        RoutesManager.apply {
            app = this@HelloApplication
        }.run {
            initSplashScreen(stage)
        }
    }
}

fun main() {
    Application.launch(HelloApplication::class.java)
}