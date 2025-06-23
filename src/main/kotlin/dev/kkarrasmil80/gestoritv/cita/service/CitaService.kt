package dev.kkarrasmil80.gestoritv.cita.service

import com.github.michaelbull.result.Result
import dev.kkarrasmil80.gestoritv.cita.error.CitaError
import dev.kkarrasmil80.gestoritv.cita.models.Cita

interface CitaService {
    // Devuelve todas las citas o error
    fun getAll(): Result<List<Cita>, CitaError>

    // Devuelve una cita por id o error si no existe
    fun getById(id: Int): Result<Cita, CitaError>

    // Inserta una cita y devuelve el id o error
    fun insert(cita: Cita): Result<Int, CitaError>

    // Elimina una cita por id y devuelve filas afectadas o error
    fun deleteById(id: Int): Result<Int, CitaError>

    // Elimina todas las citas y devuelve filas afectadas o error
    fun deleteAll(): Result<Int, CitaError>
}
