package dev.kkarrasmil80.gestoritv.cita.validator

import com.github.michaelbull.result.Result

interface ValidatorCita<T, E> {
    fun validate (t: T): Result<T, E>
}