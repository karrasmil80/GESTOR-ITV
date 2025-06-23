package dev.kkarrasmil80.gestoritv.vehiculo.mapper

import dev.kkarrasmil80.gestoritv.vehiculo.dao.VehiculoEntity
import dev.kkarrasmil80.gestoritv.vehiculo.dto.VehiculoDto
import dev.kkarrasmil80.gestoritv.vehiculo.models.Vehiculo
import dev.kkarrasmil80.gestoritv.vehiculo.models.VehiculoElectrico
import dev.kkarrasmil80.gestoritv.vehiculo.models.VehiculoMotor
import dev.kkarrasmil80.gestoritv.vehiculo.models.VehiculoPublico

fun VehiculoEntity.toModel(): Vehiculo {
    return when (tipo.lowercase()) {
        "electrico" -> VehiculoElectrico(
            id = id,
            matricula = matricula,
            marca = marca,
            modelo = modelo,
            anio = anio,
            tipo = tipo,
            consumo = consumo!!,
            neumaticos = neumaticos,
            bateria = bateria,
            frenos = frenos
        )

        "motor" -> VehiculoMotor(
            id = id,
            matricula = matricula,
            marca = marca,
            modelo = modelo,
            anio = anio,
            tipo = tipo,
            cilindrada = cilindrada!!,
            aceite = aceite!!,
            neumaticos = neumaticos,
            bateria = bateria,
            frenos = frenos
        )

        "publico" -> VehiculoPublico(
            id = id,
            matricula = matricula,
            marca = marca,
            modelo = modelo,
            anio = anio,
            tipo = tipo,
            capacidad = capacidad!!,
            neumaticos = neumaticos,
            bateria = bateria,
            frenos = frenos
        )
        else -> throw IllegalArgumentException("Tipo de vehículo no reconocido: $tipo")
    }
}

fun Vehiculo.toEntity(): VehiculoEntity {
    return when (this) {
        is VehiculoElectrico -> VehiculoEntity(
            id = id,
            matricula = matricula,
            marca = marca,
            modelo = modelo,
            anio = anio,
            tipo = "electrico",
            consumo = consumo,
            cilindrada = null,
            capacidad = null,
            neumaticos = neumaticos,
            bateria = bateria,
            frenos = frenos,
            aceite = null
        )
        is VehiculoMotor -> VehiculoEntity(
            id = id,
            matricula = matricula,
            marca = marca,
            modelo = modelo,
            anio = anio,
            tipo = "motor",
            consumo = null,
            cilindrada = cilindrada,
            capacidad = null,
            neumaticos = neumaticos,
            bateria = bateria,
            frenos = false,
            aceite = aceite
        )
        is VehiculoPublico -> VehiculoEntity(
            id = id,
            matricula = matricula,
            marca = marca,
            modelo = modelo,
            anio = anio,
            tipo = "publico",
            consumo = null,
            cilindrada = null,
            capacidad = capacidad,
            neumaticos = neumaticos,
            bateria = bateria,
            frenos = frenos,
            aceite = null
        )
        else -> throw IllegalArgumentException("Tipo de vehículo no soportado")
    }
}

fun Vehiculo.toDto(): VehiculoDto {
    return when (this) {
        is VehiculoElectrico -> VehiculoDto(
            id = id,
            matricula = matricula,
            marca = marca,
            modelo = modelo,
            anio = anio,
            tipo = "electrico",
            consumo = consumo,
            cilindrada = null,
            capacidad = null,
            neumaticos = neumaticos,
            bateria = bateria,
            frenos = frenos,
            aceite = null
        )
        is VehiculoMotor -> VehiculoDto(
            id = id,
            matricula = matricula,
            marca = marca,
            modelo = modelo,
            anio = anio,
            tipo = "motor",
            consumo = null,
            cilindrada = cilindrada,
            capacidad = null,
            neumaticos = neumaticos,
            bateria = bateria,
            frenos = frenos,
            aceite = aceite
        )
        is VehiculoPublico -> VehiculoDto(
            id = id,
            matricula = matricula,
            marca = marca,
            modelo = modelo,
            anio = anio,
            tipo = "publico",
            consumo = null,
            cilindrada = null,
            capacidad = capacidad,
            neumaticos = neumaticos,
            bateria = bateria,
            frenos = frenos,
            aceite = null
        )
        else -> throw IllegalArgumentException("Tipo de vehículo no soportado: ${this::class.simpleName}")
    }
}

fun VehiculoDto.toModel(): Vehiculo {
    return when (tipo.lowercase()) {
        "electrico" -> VehiculoElectrico(
            id = id,
            matricula = matricula,
            marca = marca,
            modelo = modelo,
            anio = anio,
            tipo = tipo,
            consumo = consumo ?: "",
            neumaticos = neumaticos,
            bateria = bateria,
            frenos = frenos
        )
        "motor" -> VehiculoMotor(
            id = id,
            matricula = matricula,
            marca = marca,
            modelo = modelo,
            anio = anio,
            tipo = tipo,
            cilindrada = cilindrada ?: 0,
            aceite = aceite ?: 0,
            neumaticos = neumaticos,
            bateria = bateria,
            frenos = frenos
        )
        "publico" -> VehiculoPublico(
            id = id,
            matricula = matricula,
            marca = marca,
            modelo = modelo,
            anio = anio,
            tipo = tipo,
            capacidad = capacidad ?: 0,
            neumaticos = neumaticos,
            bateria = bateria,
            frenos = frenos
        )
        else -> throw IllegalArgumentException("Tipo de vehículo no reconocido: $tipo")
    }
}


