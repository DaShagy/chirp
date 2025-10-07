package com.jjasystems.chirp.chat.presentation.profile

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import chirp.feature.chat.presentation.generated.resources.Res
import chirp.feature.chat.presentation.generated.resources.error_current_password_incorrect
import chirp.feature.chat.presentation.generated.resources.error_new_password_conflict
import com.jjasystems.chirp.chat.domain.participant.ChatParticipantRepository
import com.jjasystems.chirp.core.domain.auth.AuthService
import com.jjasystems.chirp.core.domain.auth.SessionStorage
import com.jjasystems.chirp.core.domain.util.DataError
import com.jjasystems.chirp.core.domain.util.onFailure
import com.jjasystems.chirp.core.domain.util.onSuccess
import com.jjasystems.chirp.core.domain.validation.PasswordValidator
import com.jjasystems.chirp.core.presentation.util.UiText
import com.jjasystems.chirp.core.presentation.util.toUiText
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val authService: AuthService,
    private val chatParticipantRepository: ChatParticipantRepository,
    private val sessionStorage: SessionStorage
) : ViewModel() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(ProfileState())
    val state = combine(
        _state,
        sessionStorage.observeAuthInfo()
    ) { currentState, authInfo ->
        if (authInfo != null) {
            currentState.copy(
                username = authInfo.user.username,
                emailTextState = TextFieldState(initialText = authInfo.user.email),
                profilePictureUrl = authInfo.user.profilePictureUrl
            )
        } else currentState
    }
        .onStart {
            if (!hasLoadedInitialData) {
                observeCanChangePassword()
                fetchLocalParticipantDetails()
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = ProfileState()
        )

    fun onAction(action: ProfileAction) {
        when (action) {
            is ProfileAction.OnChangePasswordClick -> changePassword()
            ProfileAction.OnToggleCurrentPasswordVisibility -> toggleCurrentPasswordVisibility()
            ProfileAction.OnToggleNewPasswordVisibility -> toggleNewPasswordVisibility()
            else -> Unit
        }
    }

    private fun changePassword() {
        if(!state.value.canChangePassword || state.value.isChangingPassword) return

        _state.update { it.copy(
            isChangingPassword = true,
            isPasswordChangeSuccessful = false
        ) }

        viewModelScope.launch {
            val currentPassword = state.value.currentPasswordState.text.toString()
            val newPassword = state.value.newPasswordState.text.toString()

            authService
                .changePassword(
                    currentPassword = currentPassword,
                    newPassword = newPassword
                )
                .onSuccess {
                    state.value.currentPasswordState.clearText()
                    state.value.newPasswordState.clearText()

                    _state.update { it.copy(
                        isChangingPassword = false,
                        newPasswordError = null,
                        isNewPasswordVisible = false,
                        isCurrentPasswordVisible = false,
                        isPasswordChangeSuccessful = true
                    ) }
                }
                .onFailure { error ->
                    val errorMessage = when(error) {
                        DataError.Remote.UNAUTHORIZED -> {
                            UiText.Resource(Res.string.error_current_password_incorrect)
                        }
                        DataError.Remote.CONFLICT -> {
                            UiText.Resource(Res.string.error_new_password_conflict)

                        }
                        else -> error.toUiText()
                    }

                    _state.update { it.copy(
                        newPasswordError = errorMessage,
                        isChangingPassword = false,
                        isPasswordChangeSuccessful = false
                    ) }
                }
        }
    }

    private fun observeCanChangePassword() {
        val isCurrentPasswordValidFlow = snapshotFlow {
            state.value.currentPasswordState.text.toString()
        }.map { it.isNotBlank() }.distinctUntilChanged()

        val isNewPasswordValidFlow = snapshotFlow {
            state.value.newPasswordState.text.toString()
        }.map {
            PasswordValidator.validate(it).isValidPassword
        }.distinctUntilChanged()

        combine(
            isCurrentPasswordValidFlow,
            isNewPasswordValidFlow
        ) { isCurrentPasswordValid, isNewPasswordValid ->
            _state.update { it.copy(
                canChangePassword = isCurrentPasswordValid && isNewPasswordValid
            ) }
        }.launchIn(viewModelScope)
    }

    private fun fetchLocalParticipantDetails() {
        viewModelScope.launch {
            chatParticipantRepository.fetchLocalParticipant()
        }
    }

    private fun toggleCurrentPasswordVisibility() {
        _state.update { it.copy(
            isCurrentPasswordVisible = !it.isCurrentPasswordVisible
        ) }
    }

    private fun toggleNewPasswordVisibility() {
        _state.update { it.copy(
            isNewPasswordVisible = !it.isNewPasswordVisible
        ) }
    }
}