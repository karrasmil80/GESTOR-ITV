package dev.kkarrasmil80.gestoritv.cita.mapper

import dev.kkarrasmil80.gestoritv.cita.dao.CitaEntity
import dev.kkarrasmil80.gestoritv.cita.models.Cita
import dev.kkarrasmil80.gestoritv.vehiculo.models.Vehiculo

// Convierte CitaEntity a modelo Cita con su Vehiculo asociado
fun CitaEntity.toModel(vehiculo: Vehiculo): Cita {
    return Cita(
        id = id,
        fechaCita = fechaCita,
        hora = hora,
        vehiculo = vehiculo,
    )
}

// Convierte modelo Cita a entidad CitaEntity
fun Cita.toEntity(): CitaEntity {
    return CitaEntity(
        id = id,
        fechaCita = fechaCita,
        hora = hora,
        vehiculoId = vehiculo!!.id,
    )
}
