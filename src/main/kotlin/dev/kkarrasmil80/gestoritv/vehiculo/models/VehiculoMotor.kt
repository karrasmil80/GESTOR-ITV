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
    val cilindrada : Int,
    val aceite : Int,
    val neumaticos : Boolean,
    val bateria : Boolean,
    val frenos : Boolean
) : Vehiculo(id, matricula, marca, modelo, anio, tipo)