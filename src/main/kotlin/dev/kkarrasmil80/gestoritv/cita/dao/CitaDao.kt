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

    // Devuelve todas las citas
    @SqlQuery("SELECT * FROM citas")
    fun getAll(): List<CitaEntity>

    // Devuelve una cita por su id
    @SqlQuery("SELECT * FROM citas WHERE id = :id")
    fun getById(id: Int): CitaEntity

    // Inserta una cita y devuelve el id generado
    @SqlUpdate("INSERT INTO citas (id, fechaCita, hora, vehiculoId) VALUES (:id, :fechaCita , :hora, :vehiculoId) ")
    @GetGeneratedKeys
    fun insert(@BindBean cita: CitaEntity): Int

    // Elimina una cita por su id y devuelve filas afectadas
    @SqlUpdate("DELETE FROM citas WHERE id = :id")
    fun deleteById(@Bind("id")id: Int) : Int

    // Elimina todas las citas y devuelve filas afectadas
    @SqlUpdate("DELETE FROM citas")
    fun deleteAll() : Int

    // Cuenta cuántas citas existen para una fecha y hora dadas
    @SqlQuery("SELECT COUNT(*) FROM citas WHERE fechaCita = :fecha AND hora = :hora")
    fun countByFechaAndHora(@Bind("fecha") fecha: String, @Bind("hora") hora: String): Int

}
