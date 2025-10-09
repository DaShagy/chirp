package com.jjasystems.chirp.chat.presentation.chat_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jjasystems.chirp.chat.domain.chat.ChatRepository
import com.jjasystems.chirp.chat.domain.notification.DeviceTokenService
import com.jjasystems.chirp.chat.presentation.mapper.toUiModel
import com.jjasystems.chirp.core.domain.auth.AuthService
import com.jjasystems.chirp.core.domain.auth.SessionStorage
import com.jjasystems.chirp.core.domain.util.onFailure
import com.jjasystems.chirp.core.domain.util.onSuccess
import com.jjasystems.chirp.core.presentation.util.toUiText
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChatListViewModel(
    private val repository: ChatRepository,
    private val sessionStorage: SessionStorage,
    private val deviceTokenService: DeviceTokenService,
    private val authService: AuthService
) : ViewModel() {

    private var hasLoadedInitialData = false

    private val eventChannel = Channel<ChatListEvent>()
    val events = eventChannel.receiveAsFlow()

    private val _state = MutableStateFlow(ChatListState())
    val state = combine(
        _state,
        repository.getChats(),
        sessionStorage.observeAuthInfo()
    ) { currentState, chats, authInfo ->

        if (authInfo == null) {
            return@combine ChatListState()
        }

        currentState.copy(
            chats = chats.map { it.toUiModel(authInfo.user.id) },
            localParticipant = authInfo.user.toUiModel()
        )
    }
        .onStart {
            if (!hasLoadedInitialData) {
                loadChats()
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = ChatListState()
        )

    fun onAction(action: ChatListAction) {
        when (action) {
            is ChatListAction.OnSelectChat -> {
                _state.update { it.copy(
                    selectedChatId = action.chatId
                ) }
            }
            ChatListAction.OnUserAvatarClick -> {
                _state.update { it.copy(
                    isUserMenuOpen = true
                ) }
            }
            ChatListAction.OnLogoutClick -> showLogoutConfirmation()
            ChatListAction.OnProfileSettings,
            ChatListAction.OnDismissUserMenu -> {
                _state.update { it.copy(
                    isUserMenuOpen = false
                ) }
            }
            ChatListAction.OnConfirmLogoutClick -> logout()
            ChatListAction.OnDismissLogoutDialog -> {
                _state.update { it.copy(
                    showLogoutConfirmation = false
                ) }
            }
            else -> Unit
        }
    }

    private fun loadChats() {
        viewModelScope.launch {
            repository.fetchChats()
        }
    }

    private fun showLogoutConfirmation() {
        _state.update { it.copy(
            isUserMenuOpen = false,
            showLogoutConfirmation = true
        ) }
    }

    private fun logout() {
        _state.update { it.copy(
            showLogoutConfirmation = false
        ) }

        viewModelScope.launch {
            val authInfo = sessionStorage.observeAuthInfo().first()
            val refreshToken = authInfo?.refreshToken ?: return@launch

            deviceTokenService
                .unregisterToken(refreshToken)
                .onSuccess {
                    authService
                        .logout(refreshToken)
                        .onSuccess {
                            sessionStorage.set(null)
                            repository.deleteAllChats()
                            eventChannel.send(ChatListEvent.OnLogoutSuccess)
                        }
                        .onFailure { error ->
                            eventChannel.send(ChatListEvent.OnLogoutError(error.toUiText()))
                        }
                }
                .onFailure { error ->
                    eventChannel.send(ChatListEvent.OnLogoutError(error.toUiText()))
                }
        }
    }

}