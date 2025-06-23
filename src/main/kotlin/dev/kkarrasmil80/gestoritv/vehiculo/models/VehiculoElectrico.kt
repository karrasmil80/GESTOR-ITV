package dev.kkarrasmil80.gestoritv.vehiculo.models

/**
 * Modelo para vehículos eléctricos.
 * Extiende de la clase Vehiculo.
 *
 * @param consumo Consumo energético del vehículo.
 * @param neumaticos Estado de los neumáticos (true si están bien).
 * @param bateria Estado de la batería (true si está bien).
 * @param frenos Estado de los frenos (true si están bien).
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