package com.jjasystems.chirp.auth.presentation.register_success

sealed interface RegisterSuccessAction {
    data object OnLoginClick: RegisterSuccessAction
    data object OnResendVerificationEmailClick: RegisterSuccessAction
}