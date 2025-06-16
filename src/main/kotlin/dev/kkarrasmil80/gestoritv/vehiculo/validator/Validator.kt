package dev.kkarrasmil80.gestoritv.vehiculo.validator

import com.github.michaelbull.result.Result

interface Validator<T, E> {
    fun validate (t: T): Result<T, E>
}