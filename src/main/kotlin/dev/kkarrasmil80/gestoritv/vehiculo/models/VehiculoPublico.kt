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
    val capacidad : Int
) : Vehiculo(id, matricula, marca, modelo, anio, tipo)