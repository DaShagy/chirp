package com.jjasystems.chirp.auth.presentation.register_success

import com.jjasystems.chirp.core.presentation.util.UiText

data class RegisterSuccessState(
    val registeredEmail: String = "",
    val isResendingVerificationEmail: Boolean = false,
    val resendVerificationError: UiText? = null
)
