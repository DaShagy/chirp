package com.jjasystems.chirp.chat.data.network

import com.jjasystems.chirp.chat.domain.model.ConnectionState
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.websocket.WebSocketException
import java.io.EOFException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLException

actual class ConnectionErrorHandler {
    actual fun getConnectionStateForError(cause: Throwable): ConnectionState {
        return when(cause) {
            is ClientRequestException,
            is WebSocketException,
            is SocketException,
            is SocketTimeoutException,
            is UnknownHostException,
            is SSLException,
            is EOFException -> ConnectionState.NETWORK_ERROR
            else -> ConnectionState.UNKNOWN_ERROR
        }
    }

    actual fun transformException(exception: Exception): Throwable {
        return exception
    }

    actual fun isRetriableError(cause: Throwable): Boolean {
        return when(cause) {
            is SocketTimeoutException,
            is WebSocketException,
            is SocketException,
            is EOFException -> true
            else -> false
        }
    }
}