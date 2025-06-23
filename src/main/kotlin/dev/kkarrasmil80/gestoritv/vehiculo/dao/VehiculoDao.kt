package dev.kkarrasmil80.gestoritv.vehiculo.dao

import org.jdbi.v3.sqlobject.customizer.Bind
import org.jdbi.v3.sqlobject.customizer.BindBean
import org.jdbi.v3.sqlobject.kotlin.RegisterKotlinMapper
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys
import org.jdbi.v3.sqlobject.statement.SqlQuery
import org.jdbi.v3.sqlobject.statement.SqlUpdate

@RegisterKotlinMapper(VehiculoEntity::class)
interface VehiculoDao  {

    // Obtiene todos los vehículos
    @SqlQuery("SELECT * FROM vehiculos")
    fun getAll(): List<VehiculoEntity>

    // Obtiene un vehículo por su id
    @SqlQuery("SELECT * FROM vehiculos WHERE id = :id")
    fun getById(id: Int): VehiculoEntity

    // Inserta un nuevo vehículo y devuelve su id generado
    @SqlUpdate("""
    INSERT INTO vehiculos (
        id, matricula, marca, modelo, anio, tipo,
        consumo, cilindrada, capacidad,
        neumaticos, bateria, frenos, aceite
    )
    VALUES (
        :id, :matricula, :marca, :modelo, :anio, :tipo,
        :consumo, :cilindrada, :capacidad,
        :neumaticos, :bateria, :frenos, :aceite
    )
""")
    @GetGeneratedKeys
    fun insert(@BindBean vehiculo: VehiculoEntity): Int

    // Elimina un vehículo por su id
    @SqlUpdate("DELETE FROM vehiculos WHERE id = :id")
    fun deleteById(@Bind("id") id: Int) : Int

    // Elimina todos los vehículos
    @SqlUpdate("DELETE FROM vehiculos")
    fun deleteAll(): Int
}

