package dev.kkarrasmil80.gestoritv.vehiculo.models

/**
 * Clase base abstracta para todos los tipos de vehículos.
 * Contiene las propiedades comunes a cualquier vehículo.
 */
abstract class Vehiculo(
    open val id : Int,
    open val matricula : String,
    open val marca : String,
    open val modelo : String,
    open val anio : Int,
    open val tipo : String
)
