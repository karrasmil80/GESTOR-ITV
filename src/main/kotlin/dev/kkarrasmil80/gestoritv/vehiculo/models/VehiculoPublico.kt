package dev.kkarrasmil80.gestoritv.vehiculo.models

/**
 *
 */

class VehiculoPublico(
    id: Int,
    matricula: String,
    marca: String,
    modelo: String,
    anio: Int,
    tipo : String,
    val capacidad : Int,
    val neumaticos : Boolean,
    val bateria : Boolean,
    val frenos : Boolean
) : Vehiculo(id, matricula, marca, modelo, anio, tipo)