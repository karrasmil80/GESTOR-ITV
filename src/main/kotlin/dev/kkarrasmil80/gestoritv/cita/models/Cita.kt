package dev.kkarrasmil80.gestoritv.cita.models

import dev.kkarrasmil80.gestoritv.vehiculo.models.Vehiculo

/**
 *
 */

data class Cita(
    val id : Int,
    val fechaCita : String,
    val hora : String,
    val vehiculo : Vehiculo?
)