package com.jjasystems.chirp.auth.presentation.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import chirp.feature.auth.presentation.generated.resources.Res
import chirp.feature.auth.presentation.generated.resources.create_account
import chirp.feature.auth.presentation.generated.resources.email
import chirp.feature.auth.presentation.generated.resources.email_placeholder
import chirp.feature.auth.presentation.generated.resources.forgot_password
import chirp.feature.auth.presentation.generated.resources.login
import chirp.feature.auth.presentation.generated.resources.password
import chirp.feature.auth.presentation.generated.resources.welcome_back
import com.jjasystems.chirp.core.design_system.components.brand.ChirpBrandLogo
import com.jjasystems.chirp.core.design_system.components.buttons.ChirpButton
import com.jjasystems.chirp.core.design_system.components.buttons.ChirpButtonStyle
import com.jjasystems.chirp.core.design_system.components.layouts.ChirpAdaptiveFormLayout
import com.jjasystems.chirp.core.design_system.components.layouts.ChirpSnackbarScaffold
import com.jjasystems.chirp.core.design_system.components.textfields.ChirpPasswordTextField
import com.jjasystems.chirp.core.design_system.components.textfields.ChirpTextField
import com.jjasystems.chirp.core.design_system.theme.ChirpTheme
import com.jjasystems.chirp.core.presentation.util.ObserveAsEvents
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun LoginRoot(
    viewModel: LoginViewModel = koinViewModel(),
    onLoginSuccess: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    onCreateAccountClick: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            LoginEvent.Success -> onLoginSuccess()
        }
    }

    LoginScreen(
        state = state,
        onAction = { action ->
            when (action) {
                LoginAction.OnForgotPasswordClick -> onForgotPasswordClick()
                LoginAction.OnSignUpClick -> onCreateAccountClick()
                else -> Unit
            }

            viewModel.onAction(action)
        }
    )
}

@Composable
fun LoginScreen(
    state: LoginState,
    onAction: (LoginAction) -> Unit,
) {
    ChirpSnackbarScaffold(
        snackbarHostState = remember { SnackbarHostState() },
    ) {
        ChirpAdaptiveFormLayout(
            headerText = stringResource(Res.string.welcome_back),
            errorText = state.error?.asString(),
            logo = {
                ChirpBrandLogo()
            },
            modifier = Modifier.fillMaxSize()
        ) {

            Spacer(modifier = Modifier.height(24.dp))

            ChirpTextField(
                state = state.emailTextFieldState,
                placeholder = stringResource(Res.string.email_placeholder),
                keyboardType = KeyboardType.Email,
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth(),
                title = stringResource(Res.string.email)
            )

            Spacer(modifier = Modifier.height(16.dp))

            ChirpPasswordTextField(
                state = state.passwordTextFieldState,
                placeholder = stringResource(Res.string.password),
                isPasswordVisible = state.isPasswordVisible,
                onToggleVisibilityClick = {
                    onAction(LoginAction.OnTogglePasswordVisibility)
                },
                title = stringResource(Res.string.password),
                modifier = Modifier
                    .fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(Res.string.forgot_password),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier
                    .align(alignment = Alignment.End)
                    .clickable {
                        onAction(LoginAction.OnForgotPasswordClick)
                    }
            )

            Spacer(modifier = Modifier.height(24.dp))

            ChirpButton(
                text = stringResource(Res.string.login),
                onClick = {
                    onAction(LoginAction.OnLoginClick)
                },
                enabled = state.canLogin,
                isLoading = state.isLoggingIn,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            ChirpButton(
                text = stringResource(Res.string.create_account),
                onClick = {
                    onAction(LoginAction.OnSignUpClick)
                },
                style = ChirpButtonStyle.SECONDARY,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    ChirpTheme {
        LoginScreen(
            state = LoginState(),
            onAction = {}
        )
    }
}