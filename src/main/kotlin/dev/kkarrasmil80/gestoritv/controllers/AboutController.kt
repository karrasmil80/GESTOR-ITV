package dev.kkarrasmil80.gestoritv.controllers

import com.vaadin.open.Open
import javafx.fxml.FXML
import javafx.scene.control.Hyperlink
import org.lighthousegames.logging.logging

private val logger = logging()

class AboutController {

    @FXML
    lateinit var urlHyperLink: Hyperlink

    // Inicializa el controlador y configura el evento del hyperlink
    fun initialize() {
        urlHyperLink.setOnAction {
            val url = "https://github.com/karrasmil80"
            Open.open(url)
        }
    }
}
