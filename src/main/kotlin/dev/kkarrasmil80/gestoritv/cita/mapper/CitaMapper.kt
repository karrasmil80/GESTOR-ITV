package dev.kkarrasmil80.gestoritv.cita.mapper

import dev.kkarrasmil80.gestoritv.cita.dao.CitaEntity
import dev.kkarrasmil80.gestoritv.cita.models.Cita
import dev.kkarrasmil80.gestoritv.vehiculo.models.Vehiculo

fun CitaEntity.toModel(vehiculo: Vehiculo): Cita {
    return Cita(
        id = id,
        fechaCita = fechaCita,
        hora = hora,
        vehiculo = vehiculo,
    )
}

fun Cita.toEntity(): CitaEntity {
    return CitaEntity(
        id = id,
        fechaCita = fechaCita,
        hora = hora,
        vehiculoId = vehiculo!!.id,
    )
}