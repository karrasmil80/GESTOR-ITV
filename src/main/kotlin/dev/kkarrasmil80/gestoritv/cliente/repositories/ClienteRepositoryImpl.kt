package dev.kkarrasmil80.gestoritv.cliente.repositories

import dev.kkarrasmil80.gestoritv.cliente.dao.ClienteDao
import dev.kkarrasmil80.gestoritv.cliente.mapper.toModel
import dev.kkarrasmil80.gestoritv.cliente.models.Cliente

class ClienteRepositoryImpl(
    private val dao: ClienteDao
) : ClienteRepository {

    // Busca un cliente por email y lo convierte a modelo
    override fun findByEmail(email: String): Cliente? {
        return dao.findByEmail(email)?.toModel()
    }

    // Valida login con email y password y devuelve el cliente si es correcto
    override fun login(email: String, password: String): Cliente? {
        return dao.loginOk(email, password)?.toModel()
    }
}
