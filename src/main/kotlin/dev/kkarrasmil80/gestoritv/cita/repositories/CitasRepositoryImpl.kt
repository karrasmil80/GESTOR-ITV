package dev.kkarrasmil80.gestoritv.cita.repositories

import dev.kkarrasmil80.gestoritv.cita.dao.CitaDao
import dev.kkarrasmil80.gestoritv.cita.mapper.toEntity
import dev.kkarrasmil80.gestoritv.cita.mapper.toModel
import dev.kkarrasmil80.gestoritv.cita.models.Cita
import dev.kkarrasmil80.gestoritv.vehiculo.dao.VehiculoDao
import dev.kkarrasmil80.gestoritv.vehiculo.mapper.toModel

class CitasRepositoryImpl(
    private val citaDao : CitaDao,
    private val vehiculoDao : VehiculoDao
) : CitasRepository {
    override fun getAll(): List<Cita> {
        val citasEntity = citaDao.getAll()
        return citasEntity.map { citaEntity ->
            val vehiculo = vehiculoDao.getById(citaEntity.vehiculoId)
            citaEntity.toModel(vehiculo.toModel())
        }
    }

    override fun getById(id: Int): Cita {
        val citaEntity = citaDao.getById(id)
        val vehiculo = vehiculoDao.getById(citaEntity.vehiculoId)
        return citaEntity.toModel(vehiculo.toModel())
    }

    override fun insert(cita: Cita): Int {
        return citaDao.insert(cita.toEntity())
    }
    override fun deleteById(id: Int) : Int {
        return citaDao.deleteById(id)
    }
    override fun deleteAll(): Int {
        return citaDao.deleteAll()
    }

    override fun fechaYHoraCita(hora: String, fecha: String): Boolean {
        return citaDao.countByFechaAndHora(hora, fecha) > 0
    }

}