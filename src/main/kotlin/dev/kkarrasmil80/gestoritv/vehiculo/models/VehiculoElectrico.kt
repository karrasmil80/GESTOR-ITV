package dev.kkarrasmil80.gestoritv.vehiculo.models

/**
 *
 */

class VehiculoElectrico(
    id: Int,
    matricula: String,
    marca: String,
    modelo: String,
    anio: Int,
    tipo : String,
    val consumo : String,
    val neumaticos : Boolean,
    val bateria : Boolean,
    val frenos : Boolean
) : Vehiculo(id, matricula, marca, modelo, anio, tipo)