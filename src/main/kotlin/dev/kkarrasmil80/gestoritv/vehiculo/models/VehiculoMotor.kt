package dev.kkarrasmil80.gestoritv.vehiculo.models

/**
 * Modelo para vehículos con motor de combustión.
 * Extiende la clase base Vehiculo.
 *
 * @param cilindrada Cilindrada del motor en cc.
 * @param aceite Nivel o estado del aceite.
 * @param neumaticos Estado de los neumáticos.
 * @param bateria Estado de la batería.
 * @param frenos Estado de los frenos.
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