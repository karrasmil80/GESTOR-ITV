package dev.kkarrasmil80.gestoritv.cita.dao

import dev.kkarrasmil80.gestoritv.cita.models.Cita
import dev.kkarrasmil80.gestoritv.vehiculo.models.Vehiculo
import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.customizer.BindBean
import org.jdbi.v3.sqlobject.kotlin.RegisterKotlinMapper
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys
import org.jdbi.v3.sqlobject.statement.SqlQuery
import org.jdbi.v3.sqlobject.statement.SqlUpdate

@RegisterKotlinMapper(CitaEntity::class)
interface CitaDao {

    /**
     *
     */

    @SqlQuery("SELECT * FROM citas")
    fun getAll(): List<CitaEntity>

    /**
     *
     */

    @SqlQuery("SELECT * FROM citas WHERE id = :id")
    fun getById(id: Int): CitaEntity

    /**
     *
     */

    @SqlUpdate("INSERT INTO citas (id, fechaCita, hora, vehiculoId) VALUES (:id, :fechaCita , :hora, :vehiculoId) ")
    @GetGeneratedKeys
    fun insert(@BindBean cita: CitaEntity): Int

    /**
     *
     */

    @SqlUpdate("DELETE FROM citas WHERE id = :id")
    fun deleteById(@Bind("id")id: Int) : Int

    /**
     *
     */

    @SqlUpdate("DELETE FROM citas")
    fun deleteAll() : Int

    @SqlQuery("SELECT COUNT(*) FROM citas WHERE fechaCita = :fecha AND hora = :hora")
    fun countByFechaAndHora(@Bind("fecha") fecha: String, @Bind("hora") hora: String): Int

}