package com.jjasystems.chirp.auth.presentation.reset_password

import androidx.compose.foundation.text.input.TextFieldState
import com.jjasystems.chirp.core.presentation.util.UiText

data class ResetPasswordState(
    val passwordTextFieldState: TextFieldState = TextFieldState(),
    val isLoading: Boolean = false,
    val errorText: UiText? = null,
    val isPasswordVisible: Boolean = false,
    val canSubmit: Boolean = false,
    val isResetSuccessful: Boolean = false
)