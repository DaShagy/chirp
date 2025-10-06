package com.jjasystems.chirp.chat.presentation.chat_detail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import chirp.feature.chat.presentation.generated.resources.Res
import chirp.feature.chat.presentation.generated.resources.no_messages
import chirp.feature.chat.presentation.generated.resources.no_messages_subtitle
import com.jjasystems.chirp.chat.presentation.components.EmptySection
import com.jjasystems.chirp.chat.presentation.model.ChatMessageUiModel
import org.jetbrains.compose.resources.stringResource

@Composable
fun MessageList(
    messages: List<ChatMessageUiModel>,
    messageWithOpenMenu: ChatMessageUiModel.LocalUserMessageUiModel?,
    listState: LazyListState,
    onMessageLongClick: (ChatMessageUiModel.LocalUserMessageUiModel) -> Unit,
    onRetryMessageClick: (ChatMessageUiModel.LocalUserMessageUiModel) -> Unit,
    onDeleteMessageClick: (ChatMessageUiModel.LocalUserMessageUiModel) -> Unit,
    onDismissMessageMenu: () -> Unit,
    modifier: Modifier = Modifier
) {
    if(messages.isEmpty()) {
        Box(
            modifier = modifier
                .padding(vertical = 32.dp),
            contentAlignment = Alignment.Center
        ) {
            EmptySection(
                title = stringResource(Res.string.no_messages),
                description = stringResource(Res.string.no_messages_subtitle)
            )
        }
    } else {
        LazyColumn(
            modifier = modifier,
            state = listState,
            contentPadding = PaddingValues(16.dp),
            reverseLayout = true,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(
                items = messages,
                key = { it.id }
            ) { message ->
                MessageListItem(
                    messageUi = message,
                    messageWithOpenMenu = messageWithOpenMenu,
                    onMessageLongClick = onMessageLongClick,
                    onDismissMessageMenu = onDismissMessageMenu,
                    onDeleteMessageClick = onDeleteMessageClick,
                    onRetryMessageClick = onRetryMessageClick,
                    modifier = Modifier.fillMaxWidth().animateItem()
                )
            }
        }
    }
}
