package dev.kkarrasmil80.gestoritv.cita.models

import dev.kkarrasmil80.gestoritv.vehiculo.models.Vehiculo

/**
 * Representa una cita para revisión técnica de vehículos.
 *
 * @property id Identificador único de la cita.
 * @property fechaCita Fecha de la cita en formato "yyyy-MM-dd".
 * @property hora Hora de la cita en formato "HH:mm".
 * @property vehiculo Vehículo asociado a la cita. Puede ser nulo si no está asignado.
 */

data class Cita(
    val id: Int,
    val fechaCita: String,
    val hora: String,
    val vehiculo: Vehiculo?
)
