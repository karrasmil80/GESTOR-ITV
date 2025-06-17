package dev.kkarrasmil80.gestoritv.cita.dao

import dev.kkarrasmil80.gestoritv.vehiculo.models.Vehiculo

data class CitaEntity(
    val id : Int,
    val fechaCita : String,
    val hora : String,
    val vehiculoId : Int
)