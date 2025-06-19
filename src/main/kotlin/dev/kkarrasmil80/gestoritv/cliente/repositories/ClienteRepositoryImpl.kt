package dev.kkarrasmil80.gestoritv.cliente.repositories

import dev.kkarrasmil80.gestoritv.cliente.dao.ClienteDao
import dev.kkarrasmil80.gestoritv.cliente.mapper.toModel
import dev.kkarrasmil80.gestoritv.cliente.models.Cliente

class ClienteRepositoryImpl (
    private val dao : ClienteDao
) : ClienteRepository {
    override fun findByEmail(email: String): Cliente? {
        return dao.findByEmail(email)?.toModel()
    }

    override fun login(email: String, password: String): Cliente? {
        return dao.loginOk(email, password)?.toModel()
    }
}