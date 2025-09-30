package com.jjasystems.chirp.auth.presentation.email_verification

sealed interface EmailVerificationAction {
    data object OnLoginClick: EmailVerificationAction
    data object OnCloseClick: EmailVerificationAction
}