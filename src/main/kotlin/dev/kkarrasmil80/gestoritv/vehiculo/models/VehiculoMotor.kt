package dev.kkarrasmil80.gestoritv.vehiculo.models

/**
 *
 */

class VehiculoMotor(
    id: Int,
    matricula: String,
    marca: String,
    modelo: String,
    anio: Int,
    val cilindrada : Int
) : Vehiculo(id, matricula, marca, modelo, anio)