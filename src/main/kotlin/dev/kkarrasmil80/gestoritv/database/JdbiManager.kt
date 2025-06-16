package dev.kkarrasmil80.gestoritv.database

import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.KotlinPlugin
import org.jdbi.v3.core.statement.SqlLogger
import org.jdbi.v3.core.statement.StatementContext
import org.jdbi.v3.sqlobject.SqlObjectPlugin

import org.lighthousegames.logging.logging
import java.io.File
import java.time.temporal.ChronoUnit

//Logger
private val logger = logging()

class JdbiManager (
    private val databaseUrl : String,
    private val databaseInitData : Boolean,
    private val databaseInitTables : Boolean,
    private val databaseLogger : Boolean
) {

    /**
     * para que [Jdbi] se cree cuando se necesite
     */

    val jdbi by lazy {
        Jdbi.create(this.databaseUrl)
    }

    init {

        //Instalamos los plugins necesarios para llevar acabo el programa
        jdbi.installPlugins()

        if (databaseLogger) {
            val sqlLogger: SqlLogger = object : SqlLogger {
                override fun logAfterExecution(context: StatementContext) {
                    logger.debug {
                        "Query executed: " + "sql ${context.renderedSql}, parameters ${context.binding}, timeTaken ${
                            context.getElapsedTime(
                                ChronoUnit.MILLIS
                            )
                        } ms"
                    }
                }
            }
            jdbi.setSqlLogger(sqlLogger)
        }

        /**
         * Ejecuta un script que obtiene de [config.properties]
         */

        if (databaseInitTables) {
            logger.debug { "Obteniendo tablas del programa" }
            executeSqlScriptFromResources("tables.sql")
        }

        /**
         * Ejecuta un script que obtiene de [config.properties]
         */

        if (databaseInitData) {
            logger.debug { "Database initialized" }
            executeSqlScriptFromResources("data.sql")
        }
    }

    /**
     * Busca el script en la ruta del recurso [resourcePath] y comprueba si se puede leer o ejecutar
     */

    fun executeSqlScriptFromResources(resourcePath: String) {
        logger.debug { "Ejecutando script SQL desde recursos: $resourcePath" }
        val inputStream = ClassLoader.getSystemResourceAsStream(resourcePath)?.bufferedReader()!!
        val script = inputStream.readText()
        jdbi.useHandle<Exception> { handle ->
            handle.createScript(script).execute()
        }
    }

    /**
     *  Busca el script en la ruta [scriptFilePath] y comprueba si se puede leer o ejecutar
     */

    fun executeSqlScript(scriptFilePath: String) {
        logger.debug { "Ejecutando script SQL: $scriptFilePath" }
        val script = File(scriptFilePath).readText()
        jdbi.useHandle<Exception> { handle ->
            handle.createScript(script).execute()
        }
    }
}