package com.jjasystems.chirp.chat.domain.error

import com.jjasystems.chirp.core.domain.util.Error

enum class ConnectionError: Error {
    NOT_CONNECTED,
    MESSAGE_SEND_FAILED
}