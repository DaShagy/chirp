package com.jjasystems.chirp.chat.presentation.chat_detail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jjasystems.chirp.chat.domain.model.ChatMessage
import com.jjasystems.chirp.chat.domain.model.ChatMessageDeliveryStatus
import com.jjasystems.chirp.chat.presentation.chat_detail.components.ChatDetailHeader
import com.jjasystems.chirp.chat.presentation.chat_detail.components.MessageBox
import com.jjasystems.chirp.chat.presentation.chat_detail.components.MessageList
import com.jjasystems.chirp.chat.presentation.components.ChatHeader
import com.jjasystems.chirp.chat.presentation.model.ChatMessageUiModel
import com.jjasystems.chirp.chat.presentation.model.ChatUiModel
import com.jjasystems.chirp.core.design_system.components.avatar.ChatParticipantUiModel
import com.jjasystems.chirp.core.design_system.theme.ChirpTheme
import com.jjasystems.chirp.core.design_system.theme.extended
import com.jjasystems.chirp.core.presentation.util.ObserveAsEvents
import com.jjasystems.chirp.core.presentation.util.UiText
import com.jjasystems.chirp.core.presentation.util.clearFocusOnTap
import com.jjasystems.chirp.core.presentation.util.currentDeviceConfiguration
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import kotlin.random.Random
import kotlin.time.Clock

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ChatDetailRoot(
    chatId: String?,
    isDetailPresent: Boolean,
    onBack: () -> Unit,
    viewModel: ChatDetailViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val snackbarState = remember { SnackbarHostState() }

    ObserveAsEvents(viewModel.events) { event ->
        when(event) {
            ChatDetailEvent.OnChatLeft -> onBack()
            is ChatDetailEvent.OnError -> {
                snackbarState.showSnackbar(event.error.asStringAsync())
            }
        }
    }

    LaunchedEffect(chatId) {
        viewModel.onAction(ChatDetailAction.OnSelectChat(chatId))
    }

    BackHandler(
        enabled = !isDetailPresent
    ) {
        viewModel.onAction(ChatDetailAction.OnSelectChat(null))
        onBack()
    }

    ChatDetailScreen(
        state = state,
        isDetailPresent = isDetailPresent,
        onAction = viewModel::onAction,
        snackbatState = snackbarState
    )
}

@Composable
fun ChatDetailScreen(
    state: ChatDetailState,
    isDetailPresent: Boolean,
    snackbatState: SnackbarHostState,
    onAction: (ChatDetailAction) -> Unit,
) {
    val configuration = currentDeviceConfiguration()
    val messageListState = rememberLazyListState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets.safeDrawing,
        containerColor = if(configuration.isMobile) {
            MaterialTheme.colorScheme.surface
        } else {
            MaterialTheme.colorScheme.extended.surfaceLower
        },
        snackbarHost = {
            SnackbarHost(snackbatState)
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier.clearFocusOnTap()
                .padding(innerPadding)
                .then(
                    if(configuration.isWideScreen) {
                        Modifier.padding(horizontal = 8.dp)
                    } else Modifier
                )
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                DynamicRoundedCornerColumn(
                    isCornersRounded = configuration.isWideScreen,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    ChatHeader {
                        ChatDetailHeader(
                            chat = state.chatUi,
                            isDetailPresent = isDetailPresent,
                            isChatDropdownOpen = state.isChatOptionsOpen,
                            onChatOptionsClick = {
                                onAction(ChatDetailAction.OnChatOptionsClick)
                            },
                            onDismissChatOptions = {
                                onAction(ChatDetailAction.OnDismissChatOptions)
                            },
                            onManageChatClick = {
                                onAction(ChatDetailAction.OnChatMembersClick)
                            },
                            onLeaveChatClick = {
                                onAction(ChatDetailAction.OnLeaveChatClick)
                            },
                            onBackClick = {
                                onAction(ChatDetailAction.OnBackClick)
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    MessageList(
                        messages = state.messages,
                        listState = messageListState,
                        onMessageLongClick = { message ->
                            onAction(ChatDetailAction.OnMessageLongClick(message))
                        },
                        onRetryMessageClick = { message ->
                            onAction(ChatDetailAction.OnRetryClick(message))
                        },
                        onDismissMessageMenu = {
                            onAction(ChatDetailAction.OnDismissMessageMenu)
                        },
                        onDeleteMessageClick = { message ->
                            onAction(ChatDetailAction.OnDeleteMessageClick(message))
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    )
                    AnimatedVisibility(
                        visible = !configuration.isWideScreen && state.chatUi != null
                    ) {
                        MessageBox(
                            messageTextFieldState = state.messageTextFieldState,
                            isTextInputEnabled = state.canSendMessage,
                            connectionState = state.connectionState,
                            onSendClick = {
                                onAction(ChatDetailAction.OnSendMessageClick)
                            },
                            modifier = Modifier.fillMaxWidth()
                                .padding(
                                    vertical = 8.dp,
                                    horizontal = 16.dp
                                )
                        )
                    }
                }

                if(configuration.isWideScreen) {
                    Spacer(modifier = Modifier.height(8.dp))
                }

                AnimatedVisibility(
                    visible = configuration.isWideScreen && state.chatUi != null
                ) {
                    DynamicRoundedCornerColumn(
                        isCornersRounded = configuration.isWideScreen
                    ) {
                        MessageBox(
                            messageTextFieldState = state.messageTextFieldState,
                            isTextInputEnabled = state.canSendMessage,
                            connectionState = state.connectionState,
                            onSendClick = {
                                onAction(ChatDetailAction.OnSendMessageClick)
                            },
                            modifier = Modifier.fillMaxWidth()
                                .padding(8.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DynamicRoundedCornerColumn(
    isCornersRounded: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .shadow(
                elevation = if(isCornersRounded) 8.dp else 0.dp,
                shape = if(isCornersRounded) RoundedCornerShape(16.dp) else RectangleShape,
                spotColor = Color.Black.copy(alpha = 0.2f)
            )
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = if(isCornersRounded) RoundedCornerShape(16.dp) else RectangleShape
            ),
        content = content
    )
}

@Preview
@Composable
private fun ChatDetailEmpty_Preview() {
    ChirpTheme {
        ChatDetailScreen(
            state = ChatDetailState(),
            isDetailPresent = true,
            onAction = {},
            snackbatState = remember { SnackbarHostState() }
        )
    }
}

@Preview
@Composable
private fun ChatDetailMessages_Preview() {
    ChirpTheme(
        darkTheme = true
    ) {
        ChatDetailScreen(
            state = ChatDetailState(
                messages = (1..20).map {
                    val showLocalMessage = Random.nextBoolean()
                    if(showLocalMessage) {
                        ChatMessageUiModel.LocalUserMessageUiModel(
                            id = it.toString(),
                            content = "Hello world $it",
                            deliveryStatus = ChatMessageDeliveryStatus.SENT,
                            isMenuOpen = false,
                            formattedSentTime = UiText.DynamicString("Friday, Aug 20")
                        )
                    } else {
                        ChatMessageUiModel.OtherUserMessageUiModel(
                            id = it.toString(),
                            content = "Hello world $it",
                            formattedSentTime = UiText.DynamicString("Friday, Aug 20"),
                            sender = ChatParticipantUiModel(
                                id = "1rsrhxt567",
                                username = "Shagy",
                                initials = "SH"
                            )
                        )
                    }
                },
                chatUi = ChatUiModel(
                    id = "1",
                    localParticipant = ChatParticipantUiModel(
                        id = "1",
                        username = "Shagy",
                        initials = "SH"
                    ),
                    otherParticipants = listOf(
                        ChatParticipantUiModel(
                            id = "2",
                            username = "Shagy2",
                            initials = "S2"
                        ),
                        ChatParticipantUiModel(
                            id = "3",
                            username = "Shagy3",
                            initials = "S3"
                        )
                    ),
                    lastMessage = ChatMessage(
                        id = "1",
                        chatId = "1",
                        content = "This is a last chat message that was sent by Shagy3 " +
                                "and is long enough to show ellipsis in the preview",
                        createdAt = Clock.System.now(),
                        senderId = "3",
                        deliveryStatus = ChatMessageDeliveryStatus.SENT
                    ),
                    lastMessageSenderUsername = "Shagy3"
                )
            ),
            isDetailPresent = true,
            onAction = {},
            snackbatState = remember { SnackbarHostState() }
        )
    }
}