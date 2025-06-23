package dev.kkarrasmil80.gestoritv.vehiculo.dto

import kotlinx.serialization.Serializable

//DTO para poder serializar el JSON
@Serializable
class VehiculoDto(
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
) {


}