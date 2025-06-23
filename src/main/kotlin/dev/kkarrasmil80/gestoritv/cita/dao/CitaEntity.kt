package dev.kkarrasmil80.gestoritv.cita.dao

import dev.kkarrasmil80.gestoritv.vehiculo.models.Vehiculo

// Representa la entidad Cita para la base de datos
data class CitaEntity(
    val id: Int,
    val fechaCita: String,
    val hora: String,
    val vehiculoId: Int
)
