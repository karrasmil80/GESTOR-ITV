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
    val consumo : String
) : Vehiculo(id, matricula, marca, modelo, anio,)