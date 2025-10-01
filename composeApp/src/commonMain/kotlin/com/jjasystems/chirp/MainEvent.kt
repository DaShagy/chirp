package com.jjasystems.chirp

sealed interface MainEvent {
    data object OnSessionExpired: MainEvent
}