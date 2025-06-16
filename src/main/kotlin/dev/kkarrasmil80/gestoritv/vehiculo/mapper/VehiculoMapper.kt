package dev.kkarrasmil80.gestoritv.vehiculo.mapper

import dev.kkarrasmil80.gestoritv.vehiculo.dao.VehiculoEntity
import dev.kkarrasmil80.gestoritv.vehiculo.models.Vehiculo
import dev.kkarrasmil80.gestoritv.vehiculo.models.VehiculoElectrico
import dev.kkarrasmil80.gestoritv.vehiculo.models.VehiculoMotor
import dev.kkarrasmil80.gestoritv.vehiculo.models.VehiculoPublico

fun VehiculoEntity.toModel(): Vehiculo {
    return when (tipo.lowercase()) {
        "electrico" -> VehiculoElectrico(
            id, matricula, marca, modelo, anio, consumo ?: throw IllegalArgumentException("Falta consumo para eléctrico")
        )
        "motor" -> VehiculoMotor(
            id, matricula, marca, modelo, anio, cilindrada ?: throw IllegalArgumentException("Falta cilindrada para motor")
        )
        "publico" -> VehiculoPublico(
            id, matricula, marca, modelo, anio, capacidad ?: throw IllegalArgumentException("Falta capacidad para público")
        )
        else -> throw IllegalArgumentException("Tipo de vehículo no reconocido: $tipo")
    }
}

fun Vehiculo.toEntity(): VehiculoEntity {
    return when (this) {
        is VehiculoElectrico -> VehiculoEntity(
            id = this.id,
            matricula = this.matricula,
            marca = this.marca,
            modelo = this.modelo,
            anio = this.anio,
            tipo = "electrico",
            consumo = this.consumo,
            cilindrada = null,
            capacidad = null
        )
        is VehiculoMotor -> VehiculoEntity(
            id = this.id,
            matricula = this.matricula,
            marca = this.marca,
            modelo = this.modelo,
            anio = this.anio,
            tipo = "motor",
            consumo = null,
            cilindrada = this.cilindrada,
            capacidad = null
        )
        is VehiculoPublico -> VehiculoEntity(
            id = this.id,
            matricula = this.matricula,
            marca = this.marca,
            modelo = this.modelo,
            anio = this.anio,
            tipo = "publico",
            consumo = null,
            cilindrada = null,
            capacidad = this.capacidad
        )
        else -> throw IllegalArgumentException("Tipo de vehículo no soportado")
    }
}
