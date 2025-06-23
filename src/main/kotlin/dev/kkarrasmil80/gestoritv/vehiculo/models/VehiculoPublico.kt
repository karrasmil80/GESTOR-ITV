package dev.kkarrasmil80.gestoritv.vehiculo.models

/**
 * Modelo para vehículos de transporte público
 * Extiende la claseVehiculo
 *
 * @param capacidad Capacidad máxima de pasajeros
 * @param neumaticos Estado de los neumáticos
 * @param bateria Estado de la batería
 * @param frenos Estado de los frenos
 */
class VehiculoPublico(
    id: Int,
    matricula: String,
    marca: String,
    modelo: String,
    anio: Int,
    tipo: String,
    val capacidad: Int,
    val neumaticos: Boolean,
    val bateria: Boolean,
    val frenos: Boolean
) : Vehiculo(id, matricula, marca, modelo, anio, tipo)
