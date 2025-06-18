package dev.kkarrasmil80.gestoritv.vehiculo.dao

/**
 *
 */

data class VehiculoEntity(
    val id: Int,
    val matricula: String,
    val marca: String,
    val modelo: String,
    val anio: Int,
    val tipo: String,
    val consumo: String?,
    val cilindrada: Int?,
    val capacidad: Int?,
    val neumaticos: Boolean,
    val bateria: Boolean,
    val frenos: Boolean,
    val aceite: Int?
)
