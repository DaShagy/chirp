package com.jjasystems.chirp.chat.presentation.chat_detail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jjasystems.chirp.chat.presentation.model.ChatMessageUiModel
import com.jjasystems.chirp.core.design_system.components.avatar.ChirpAvatar
import com.jjasystems.chirp.core.design_system.components.chat.ChirpChatBubble
import com.jjasystems.chirp.core.design_system.components.chat.TrianglePosition

@Composable
fun OtherUserMessage(
    message: ChatMessageUiModel.OtherUserMessageUiModel,
    color: Color,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ChirpAvatar(
            displayText = message.sender.initials,
            imageUrl = message.sender.imageUrl
        )

        ChirpChatBubble(
            messageContent = message.content,
            sender = message.sender.username,
            trianglePosition = TrianglePosition.LEFT,
            formattedDateTime = message.formattedSentTime.asString(),
            color = color
        )
    }
}