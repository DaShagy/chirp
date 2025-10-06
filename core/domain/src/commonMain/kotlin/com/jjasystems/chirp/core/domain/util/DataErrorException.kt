package com.jjasystems.chirp.core.domain.util

class DataErrorException(
    val error: DataError
): Exception()