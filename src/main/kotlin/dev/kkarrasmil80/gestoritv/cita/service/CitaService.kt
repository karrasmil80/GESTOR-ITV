package dev.kkarrasmil80.gestoritv.cita.service

import com.github.michaelbull.result.Result
import dev.kkarrasmil80.gestoritv.cita.error.CitaError
import dev.kkarrasmil80.gestoritv.cita.models.Cita

interface CitaService {
    fun getAll(): Result<List<Cita>, CitaError>
    fun getById(id: Int): Result<Cita, CitaError>
    fun insert(cita: Cita): Result<Cita, CitaError>
    fun deleteById(id: Int): Result<Unit, CitaError>
    fun deleteAll(): Result<Int, CitaError>
}