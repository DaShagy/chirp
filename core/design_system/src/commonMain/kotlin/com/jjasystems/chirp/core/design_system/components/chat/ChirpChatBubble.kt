package com.jjasystems.chirp.core.design_system.components.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jjasystems.chirp.core.design_system.theme.ChirpTheme
import com.jjasystems.chirp.core.design_system.theme.extended
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ChirpChatBubble(
    messageContent: String,
    sender: String,
    formattedDateTime: String,
    trianglePosition: TrianglePosition,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.extended.surfaceHigher,
    messageStatus: @Composable (() -> Unit)? = null,
    triangleSize: Dp = 16.dp,
    onLongClick: (() -> Unit)? = null
) {
    val padding = 12.dp
    Column(
        modifier = modifier
            .then(
                if(onLongClick != null) {
                    Modifier.combinedClickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = ripple(
                            color = MaterialTheme.colorScheme.extended.surfaceOutline
                        ),
                        onLongClick = onLongClick,
                        onClick = {}
                    )
                } else Modifier
            )
            .clip(
                shape = ChatBubbleShape(
                    trianglePosition = trianglePosition,
                    triangleSize = triangleSize
                )
            )
            .background(
                color = color,
            )
            .padding(
                start = if(trianglePosition == TrianglePosition.LEFT) {
                    padding + triangleSize
                } else padding,
                end = if(trianglePosition == TrianglePosition.RIGHT) {
                    padding + triangleSize
                } else padding,
                top = padding,
                bottom = padding
            ),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = sender,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.extended.textSecondary,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.widthIn(20.dp))
            Text(
                text = formattedDateTime,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.extended.textSecondary,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.End
            )
        }

        Text(
            text = messageContent,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.extended.textPrimary,
            modifier = Modifier
                .fillMaxWidth()
        )

        messageStatus?.invoke()
    }
}

@Composable
@Preview
fun ChirpLeftChatBubble_Preview() {
    ChirpTheme(
        darkTheme = true
    ) {
        ChirpChatBubble(
            messageContent = "Hello world, this is longer message that hopefully spans" +
                    "over multiple lines so we can se how it seems in the preview.",
            sender = "Sender",
            formattedDateTime = "Friday 2:20 pm.",
            trianglePosition = TrianglePosition.LEFT,
            modifier = Modifier.widthIn(250.dp),
            color = MaterialTheme.colorScheme.extended.accentGreen
        )
    }
}

@Composable
@Preview
fun ChirpRightChatBubble_Preview() {
    ChirpTheme(
        darkTheme = true
    ) {
        ChirpChatBubble(
            messageContent = "Hello world, this is longer message that hopefully spans" +
                    "over multiple lines so we can se how it seems in the preview.",
            sender = "Sender",
            formattedDateTime = "Friday 2:20 pm.",
            trianglePosition = TrianglePosition.RIGHT,
            modifier = Modifier.widthIn(250.dp),
            color = MaterialTheme.colorScheme.extended.accentTeal
        )
    }
}