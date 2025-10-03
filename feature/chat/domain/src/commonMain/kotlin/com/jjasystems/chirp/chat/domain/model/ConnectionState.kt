package com.jjasystems.chirp.chat.domain.model

enum class ConnectionState {
    DISCONNECTED,
    CONNECTING,
    CONNECTED,
    NETWORK_ERROR,
    UNKNOWN_ERROR
}