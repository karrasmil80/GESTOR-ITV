package dev.kkarrasmil80.gestoritv.cita.repositories

import dev.kkarrasmil80.gestoritv.cita.dao.CitaEntity
import dev.kkarrasmil80.gestoritv.cita.models.Cita

interface CitasRepository {
    fun getAll(): List<Cita>
    fun getById(id: Int): Cita?
    fun insert(cita: Cita): Int
    fun deleteById(id: Int): Int
    fun deleteAll(): Int
    fun fechaYHoraCita(hora: String, fecha: String): Boolean
}