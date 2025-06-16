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
    val capacidad : Int
) : Vehiculo(id, matricula, marca, modelo, anio)