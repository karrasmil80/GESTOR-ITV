package dev.kkarrasmil80.gestoritv.vehiculo.dao

import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.customizer.BindBean
import org.jdbi.v3.sqlobject.kotlin.RegisterKotlinMapper
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys
import org.jdbi.v3.sqlobject.statement.SqlQuery
import org.jdbi.v3.sqlobject.statement.SqlUpdate

/**
 *
 */

@RegisterKotlinMapper(VehiculoEntity::class)
interface VehiculoDao  {

    /**
     *
     */

    @SqlQuery("SELECT * FROM vehiculos")
    fun getAll(): List<VehiculoEntity>

    /**
     *
     */

    @SqlQuery("SELECT * FROM vehiculos WHERE id = :id")
    fun getById(id: Int): VehiculoEntity

    /**
     *
     */

    @SqlUpdate("""
    INSERT INTO vehiculos (id, matricula, marca, modelo, anio, tipo, consumo, cilindrada, capacidad)
    VALUES (:id, :matricula, :marca, :modelo, :anio, :tipo, :consumo, :cilindrada, :capacidad)
""")
    fun insert(@BindBean vehiculo: VehiculoEntity): Int



    /**
     *
     */

    @SqlUpdate("DELETE FROM vehiculos WHERE id = :id")
    fun deleteById(@Bind("id") id: Int) : Int

    /**
     *
     */

    @SqlUpdate("DELETE FROM vehiculos")
    fun deleteAll(): Int

}
