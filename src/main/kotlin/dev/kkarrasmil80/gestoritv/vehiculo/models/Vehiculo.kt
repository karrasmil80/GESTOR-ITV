package dev.kkarrasmil80.gestoritv.vehiculo.models

/**
 *
 */

abstract class Vehiculo(
    open val id : Int,
    open val matricula : String,
    open val marca : String,
    open val modelo : String,
    open val anio : Int,
    open val tipo : String
)