package dev.kkarrasmil80.gestoritv.cliente.validator

import com.github.michaelbull.result.Result

interface ValidatorCliente<T, E> {
    fun validate (t: T): Result<T, E>
}