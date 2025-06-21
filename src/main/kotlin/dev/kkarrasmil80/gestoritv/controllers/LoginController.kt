package dev.kkarrasmil80.gestoritv.controllers

import dev.kkarrasmil80.gestoritv.routes.RoutesManager
import javafx.fxml.FXML
import javafx.scene.control.Alert
import javafx.scene.control.Button
import javafx.scene.control.PasswordField
import javafx.scene.control.TextField
import org.mindrot.jbcrypt.BCrypt
import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement

class LoginController {

    @FXML
    lateinit var passwField: PasswordField

    @FXML
    lateinit var emailField: TextField

    @FXML
    lateinit var iniciarButton: Button

    @FXML
    lateinit var cancelarButton: Button

    private lateinit var connection: Connection

    fun initialize() {
        connection = DriverManager.getConnection("jdbc:h2:mem:itv;DB_CLOSE_DELAY=-1")
        val statement = connection.createStatement()

        statement.executeUpdate(
            """
            CREATE TABLE IF NOT EXISTS cliente (
                id INT AUTO_INCREMENT PRIMARY KEY,
                nombre VARCHAR(100),
                email VARCHAR(100) UNIQUE,
                password VARCHAR(255)
            );
            """.trimIndent()
        )

        // Inserta usuario dummy con contraseña hasheada (una sola vez)
        statement.executeUpdate(
            """
            INSERT INTO cliente (nombre, email, password) VALUES (
                'Juan', 
                'pablo@kkarra.com', 
                '${BCrypt.hashpw("123456", BCrypt.gensalt())}'
            );
            """.trimIndent()
        )

        statement.close()
    }
    fun onLoginClick() {
        iniciarButton.setOnAction {
            val email = emailField.text.trim()
            val password = passwField.text.trim()

            if (verificarCredenciales(email, password)) {
                Alert(Alert.AlertType.INFORMATION, "Inicio de sesión exitoso").show()
                RoutesManager.initMainViewStage()
            } else {
                Alert(Alert.AlertType.ERROR, "Credenciales incorrectas").show()
            }
        }
    }

    fun cancelarButtonClick() {
        RoutesManager.onAppExit()
    }

    private fun verificarCredenciales(email: String, password: String): Boolean {
        val input: PreparedStatement = connection.prepareStatement(
            "SELECT password FROM cliente WHERE email = ?"
        )
        input.setString(1, email)
        val pass = input.executeQuery()

        val esValido = if (pass.next()) {
            val hash = pass.getString("password")
            BCrypt.checkpw(password, hash)
        } else {
            false
        }

        pass.close()
        input.close()

        return esValido
    }
}
