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

    // Obtiene todas las citas con su vehículo asociado
    override fun getAll(): List<Cita> {
        val citasEntity = citaDao.getAll()
        return citasEntity.map { citaEntity ->
            val vehiculo = vehiculoDao.getById(citaEntity.vehiculoId)
            citaEntity.toModel(vehiculo.toModel())
        }
    }

    // Obtiene una cita por id con su vehículo
    override fun getById(id: Int): Cita {
        val citaEntity = citaDao.getById(id)
        val vehiculo = vehiculoDao.getById(citaEntity.vehiculoId)
        return citaEntity.toModel(vehiculo.toModel())
    }

    // Inserta una cita y devuelve el id generado
    override fun insert(cita: Cita): Int {
        return citaDao.insert(cita.toEntity())
    }

    // Elimina una cita por id y devuelve filas afectadas
    override fun deleteById(id: Int) : Int {
        return citaDao.deleteById(id)
    }

    // Elimina todas las citas y devuelve filas afectadas
    override fun deleteAll(): Int {
        return citaDao.deleteAll()
    }

    // Comprueba si ya existe una cita con fecha y hora dada
    override fun fechaYHoraCita(hora: String, fecha: String): Boolean {
        return citaDao.countByFechaAndHora(hora, fecha) > 0
    }

}
