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

    // Conexión a la base de datos
    private lateinit var connection: Connection

    // Método que se ejecuta al inicializar el controlador
    fun initialize() {
        // Crear conexión a base de datos en memoria H2
        connection = DriverManager.getConnection("jdbc:h2:mem:itv;DB_CLOSE_DELAY=-1")


        initEvents()

        // Crear un statement para ejecutar comandos SQL
        val statement = connection.createStatement()

        // Crear tabla 'cliente' si no existe con columnas id, nombre, email y password
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

        // Insertar un usuario de prueba con contraseña encriptada
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


    fun initEvents(){
        onLoginClick()
    }

    // Listener para el botón de iniciar sesión
    fun onLoginClick() {
        iniciarButton.setOnAction {
            val email = emailField.text.trim()
            val password = passwField.text.trim()

            // Verificar credenciales con la base de datos
            if (verificarCredenciales(email, password)) {
                // Si es válido, abrir ventana principal y cerrar la actual
                RoutesManager.initMainViewStage()
                RoutesManager.escenaPrincipal.close()
            } else {
                // Si no, mostrar alerta de error
                Alert(Alert.AlertType.ERROR, "Credenciales incorrectas").show()
            }
        }
    }

    // Método para el botón cancelar (cerrar app)
    fun onCancelarButtonClick() {
        RoutesManager.onAppExit()
    }

    // Función que valida usuario y contraseña en la base de datos
    private fun verificarCredenciales(email: String, password: String): Boolean {
        val input: PreparedStatement = connection.prepareStatement(
            "SELECT password FROM cliente WHERE email = ?"
        )
        input.setString(1, email)
        val pass = input.executeQuery()

        // Verificar si existe usuario y comparar hash con la contraseña ingresada
        val esValido = if (pass.next()) {
            val hash = pass.getString("password")
            BCrypt.checkpw(password, hash)
        } else {
            false // No existe usuario con ese email
        }


        pass.close()
        input.close()

        return esValido
    }
}
