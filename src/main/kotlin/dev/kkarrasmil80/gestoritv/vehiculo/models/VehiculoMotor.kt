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
    tipo : String,
    val cilindrada : Int
) : Vehiculo(id, matricula, marca, modelo, anio, tipo)