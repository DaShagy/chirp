package com.jjasystems.chirp.core.data.network

import com.jjasystems.chirp.core.domain.util.DataError
import com.jjasystems.chirp.core.domain.util.Result
import io.ktor.client.statement.HttpResponse

actual suspend fun <T> platformSafeCall(
    execute: suspend () -> HttpResponse,
    handleResponse: suspend (HttpResponse) -> Result<T, DataError.Remote>
): Result<T, DataError.Remote> {
    TODO()
}