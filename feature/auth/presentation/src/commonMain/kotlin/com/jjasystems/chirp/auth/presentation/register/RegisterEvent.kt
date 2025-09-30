package com.jjasystems.chirp.auth.presentation.register

sealed interface RegisterEvent {
    data class Success(val email: String): RegisterEvent
}