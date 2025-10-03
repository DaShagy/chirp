package com.jjasystems.chirp.chat.presentation.chat_detail.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jjasystems.chirp.chat.domain.model.ChatMessageDeliveryStatus
import com.jjasystems.chirp.chat.presentation.model.ChatMessageUiModel
import com.jjasystems.chirp.core.design_system.components.avatar.ChatParticipantUiModel
import com.jjasystems.chirp.core.design_system.theme.ChirpTheme
import com.jjasystems.chirp.core.design_system.theme.extended
import com.jjasystems.chirp.core.presentation.util.UiText
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun MessageListItem(
    messageUi: ChatMessageUiModel,
    onMessageLongClick: () -> Unit,
    onDismissMessageMenu: () -> Unit,
    onRetryClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
    ) {
        when(messageUi) {
            is ChatMessageUiModel.DateSeparatorUiModel -> {
                DateSeparator(
                    date = messageUi.date.asString(),
                    modifier = Modifier.fillMaxWidth()
                )
            }
            is ChatMessageUiModel.LocalUserMessageUiModel -> {
                LocalUserMessage(
                    message = messageUi,
                    onMessageLongClick = onMessageLongClick,
                    onDismissMessageMenu = onDismissMessageMenu,
                    onDeleteClick = onDeleteClick,
                    onRetryClick = onRetryClick
                )
            }
            is ChatMessageUiModel.OtherUserMessageUiModel -> {
                OtherUserMessage(
                    message = messageUi
                )
            }
        }
    }
}

@Composable
private fun DateSeparator(
    date: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        HorizontalDivider(modifier = Modifier.weight(1f))
        Text(
            text = date,
            modifier = Modifier
                .padding(horizontal = 40.dp),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.extended.textPlaceholder
        )
        HorizontalDivider(modifier = Modifier.weight(1f))
    }
}


@Composable
@Preview
fun MessageListItemLocalSending_Preview() {
    ChirpTheme {
        MessageListItem(
            ChatMessageUiModel.LocalUserMessageUiModel(
                id = "1",
                content = "This is a preview message that probably will span multiple lines",
                deliveryStatus = ChatMessageDeliveryStatus.SENDING,
                formattedSentTime = UiText.DynamicString("Friday 2:20 pm."),
                isMenuOpen = false
            ),
            onMessageLongClick = {},
            onDismissMessageMenu = {},
            onDeleteClick = {},
            onRetryClick = {}
        )
    }
}

@Composable
@Preview
fun MessageListItemLocalSent_Preview() {
    ChirpTheme {
        MessageListItem(
            ChatMessageUiModel.LocalUserMessageUiModel(
                id = "1",
                content = "This is a preview message that probably will span multiple lines",
                deliveryStatus = ChatMessageDeliveryStatus.SENT,
                formattedSentTime = UiText.DynamicString("Friday 2:20 pm."),
                isMenuOpen = false
            ),
            onMessageLongClick = {},
            onDismissMessageMenu = {},
            onDeleteClick = {},
            onRetryClick = {}
        )
    }
}

@Composable
@Preview
fun MessageListItemLocalFailed_Preview() {
    ChirpTheme {
        MessageListItem(
            ChatMessageUiModel.LocalUserMessageUiModel(
                id = "1",
                content = "This is a preview message that probably will span multiple lines",
                deliveryStatus = ChatMessageDeliveryStatus.FAILED,
                formattedSentTime = UiText.DynamicString("Friday 2:20 pm."),
                isMenuOpen = false
            ),
            onMessageLongClick = {},
            onDismissMessageMenu = {},
            onDeleteClick = {},
            onRetryClick = {}
        )
    }
}

@Composable
@Preview
fun MessageListItemOpenMenu_Preview() {
    ChirpTheme {
        Box(
            modifier = Modifier.height(400.dp)
        ) {
            MessageListItem(
                ChatMessageUiModel.LocalUserMessageUiModel(
                    id = "1",
                    content = "This is a preview message that probably will span multiple lines",
                    deliveryStatus = ChatMessageDeliveryStatus.SENT,
                    formattedSentTime = UiText.DynamicString("Friday 2:20 pm."),
                    isMenuOpen = true
                ),
                onMessageLongClick = {},
                onDismissMessageMenu = {},
                onDeleteClick = {},
                onRetryClick = {}
            )
        }
    }
}


@Composable
@Preview
fun MessageListItemOther_Preview() {
    ChirpTheme {
        MessageListItem(
            ChatMessageUiModel.OtherUserMessageUiModel(
                id = "1",
                content = "This is a preview message that probably will span multiple lines",
                formattedSentTime = UiText.DynamicString("Friday 2:20 pm."),
                sender = ChatParticipantUiModel(
                    id = "1",
                    username = "Shagy",
                    initials = "SH"
                )
            ),
            onMessageLongClick = {},
            onDismissMessageMenu = {},
            onDeleteClick = {},
            onRetryClick = {}
        )
    }
}