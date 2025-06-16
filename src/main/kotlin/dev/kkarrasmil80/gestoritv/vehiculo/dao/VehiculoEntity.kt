package dev.kkarrasmil80.gestoritv.vehiculo.dao

/**
 *
 */

open class VehiculoEntity(
    val id: Int,
    val matricula: String,
    val marca: String,
    val modelo: String,
    val anio: Int,
    val tipo : String,
    val consumo : String?,
    val cilindrada : Int?,
    val capacidad : Int?,
)