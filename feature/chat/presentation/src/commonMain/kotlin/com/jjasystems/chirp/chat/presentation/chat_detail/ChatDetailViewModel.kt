package com.jjasystems.chirp.chat.presentation.chat_detail

import androidx.compose.foundation.text.input.clearText
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jjasystems.chirp.chat.domain.chat.ChatConnectionClient
import com.jjasystems.chirp.chat.domain.chat.ChatRepository
import com.jjasystems.chirp.chat.domain.message.MessageRepository
import com.jjasystems.chirp.chat.domain.model.ChatMessage
import com.jjasystems.chirp.chat.domain.model.ConnectionState
import com.jjasystems.chirp.chat.domain.model.OutgoingNewMessage
import com.jjasystems.chirp.chat.presentation.mapper.toUiModel
import com.jjasystems.chirp.chat.presentation.model.ChatMessageUiModel
import com.jjasystems.chirp.core.domain.auth.SessionStorage
import com.jjasystems.chirp.core.domain.util.onFailure
import com.jjasystems.chirp.core.domain.util.onSuccess
import com.jjasystems.chirp.core.presentation.util.toUiText
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalCoroutinesApi::class, ExperimentalUuidApi::class)
class ChatDetailViewModel(
    private val chatRepository: ChatRepository,
    private val sessionStorage: SessionStorage,
    private val messageRepository: MessageRepository,
    private val connectionClient: ChatConnectionClient
) : ViewModel() {

    private val eventChannel = Channel<ChatDetailEvent>()
    val events = eventChannel.receiveAsFlow()

    private val _chatId = MutableStateFlow<String?>(null)
    private var hasLoadedInitialData = false

    private val canSendMessage = snapshotFlow { _state.value.messageTextFieldState.text.toString() }
        .map { it.isBlank() }
        .combine(connectionClient.connectionState) { isMessageBlank, connectionState ->
            !isMessageBlank && connectionState == ConnectionState.CONNECTED
        }

    private val chatInfoFlow = _chatId
        .flatMapLatest { chatId ->
            if(chatId != null) {
                chatRepository.getChatInfoById(chatId)
            } else emptyFlow()
        }


    private val _state = MutableStateFlow(ChatDetailState())

    private val stateWithMessages = combine(
        _state,
        chatInfoFlow,
        sessionStorage.observeAuthInfo()
    ) { currentState, chatInfo, authInfo ->
        if(authInfo == null) {
            return@combine ChatDetailState()
        }
        currentState.copy(
            chatUi = chatInfo.chat.toUiModel(authInfo.user.id),
            messages = chatInfo.messages.map { it.toUiModel(authInfo.user.id) }
        )
    }

    val state = _chatId
        .flatMapLatest { chatId ->
            if(chatId != null) {
                stateWithMessages
            } else {
                _state
            }
        }
        .onStart {
            if (!hasLoadedInitialData) {
                observeConnectionState()
                observeChatMessages()
                observeCanSendMessage()
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = ChatDetailState()
        )


    fun onAction(action: ChatDetailAction) {
        when (action) {
            is ChatDetailAction.OnSelectChat -> switchChat(action.chatId)
            ChatDetailAction.OnChatOptionsClick -> onChatOptionsClick()
            ChatDetailAction.OnDismissChatOptions -> onDismissChatOptions()
            ChatDetailAction.OnLeaveChatClick -> onLeaveChatClick()
            ChatDetailAction.OnSendMessageClick -> sendMessage()
            is ChatDetailAction.OnRetryClick -> retryMessage(action.message)
            is ChatDetailAction.OnDeleteMessageClick -> deleteMessage(action.message)
            ChatDetailAction.OnDismissMessageMenu -> onDismissMessageMenu()
            is ChatDetailAction.OnMessageLongClick -> onMessageLongClick(action.message)
            else -> Unit
        }
    }

    private fun switchChat(chatId: String?) {
        _chatId.update { chatId }
        viewModelScope.launch {
            chatId?.let {
                chatRepository.fetchChatById(chatId)
            }
        }
    }

    private fun onChatOptionsClick() {
        _state.update { it.copy(
            isChatOptionsOpen = true
        ) }
    }

    private fun onDismissChatOptions() {
        _state.update { it.copy(
            isChatOptionsOpen = false
        ) }
    }

    private fun onLeaveChatClick() {
        val chatId = _chatId.value ?: return

        _state.update { it.copy(
            isChatOptionsOpen = false
        ) }

        viewModelScope.launch {
            chatRepository.leaveChat(chatId)
                .onSuccess {
                    _state.value.messageTextFieldState.clearText()

                    _chatId.update { null }
                    _state.update { it.copy(
                        chatUi = null,
                        messages = emptyList(),
                        bannerState = BannerState()
                    )}
                }
                .onFailure { error ->
                    eventChannel.send(
                        ChatDetailEvent.OnError(
                            error.toUiText()
                        )
                    )
                }
        }
    }

    private fun sendMessage() {
        val currentChatId = _chatId.value
        val content = state.value.messageTextFieldState.text.toString().trim()

        if (content.isBlank() || currentChatId == null) return

        viewModelScope.launch {
            val message = OutgoingNewMessage(
                chatId = currentChatId,
                messageId = Uuid.random().toString(),
                content = content
            )

            messageRepository
                .sendMessage(message)
                .onSuccess {
                    state.value.messageTextFieldState.clearText()
                }
                .onFailure { error ->
                    eventChannel.send(ChatDetailEvent.OnError(error.toUiText()))
                }
        }
    }

    private fun retryMessage(message: ChatMessageUiModel.LocalUserMessageUiModel) {
        viewModelScope.launch {
            messageRepository.retryMessage(message.id)
                .onFailure { error ->
                    eventChannel.send(ChatDetailEvent.OnError(error.toUiText()))
                }
        }
    }

    private fun deleteMessage(message: ChatMessageUiModel.LocalUserMessageUiModel) {
        viewModelScope.launch {
            messageRepository.deleteMessage(message.id)
                .onFailure { error ->
                    eventChannel.send(ChatDetailEvent.OnError(error.toUiText()))
                }
        }
    }

    private fun onDismissMessageMenu() {
        _state.update { it.copy(
            messageWithOpenMenu = null
        ) }
    }

    private fun onMessageLongClick(message: ChatMessageUiModel.LocalUserMessageUiModel) {
        _state.update { it.copy(
            messageWithOpenMenu = message
        ) }
    }

    private fun observeConnectionState() {
        connectionClient
            .connectionState
            .onEach { connectionState ->
                if(connectionState == ConnectionState.CONNECTED) {
                    _chatId.value?.let {
                        messageRepository.fetchMessages(it, null)
                    }
                }

                _state.update { it.copy(
                    connectionState = connectionState
                ) }
            }.launchIn(viewModelScope)
    }

    private fun observeChatMessages() {
        val currentMessages = state
            .map { it.messages }
            .distinctUntilChanged()

        val newMessages = _chatId
            .flatMapLatest { chatId ->
                if (chatId != null) {
                    messageRepository.getMessagesForChat(chatId)
                } else emptyFlow()
            }

        val isNearBottom = state.map { it.isNearBottom }.distinctUntilChanged()

        combine(
            currentMessages,
            newMessages,
            isNearBottom
        ) { currentMessages, newMessages, isNearBottom ->
            val lastNewId = newMessages.lastOrNull()?.message?.id
            val lasCurrentId = currentMessages.lastOrNull()?.id

            if(lastNewId != lasCurrentId && isNearBottom) {
                eventChannel.send(ChatDetailEvent.OnNewMessage)
            }
        }.launchIn(viewModelScope)
    }

    private fun observeCanSendMessage() {
        canSendMessage.onEach { canSend ->
            _state.update { it.copy(
                canSendMessage = canSend
            ) }
        }.launchIn(viewModelScope)
    }
}