package com.jjasystems.chirp.auth.presentation.register_success

sealed interface RegisterSuccessEvent {
    data object ResendVerificationEmailSuccess: RegisterSuccessEvent
}