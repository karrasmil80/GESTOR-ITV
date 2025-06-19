package dev.kkarrasmil80.gestoritv.cliente.dao

import dev.kkarrasmil80.gestoritv.cliente.models.Cliente
import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.kotlin.RegisterKotlinMapper
import org.jdbi.v3.sqlobject.statement.SqlQuery
import java.util.*

@RegisterKotlinMapper(ClienteEntity::class)
interface ClienteDao {

    @SqlQuery("SELECT * FROM cliente WHERE email = :email")
    fun findByEmail(@Bind("email")email: String): ClienteEntity?

    @SqlQuery("SELECT * FROM cliente WHERE email = :email AND password = :password")
    fun loginOk(@Bind("email") email: String, @Bind("password") password: String) : ClienteEntity?
}