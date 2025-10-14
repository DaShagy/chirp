package com.jjasystems.chirp.chat.presentation.chat_detail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.isCtrlPressed
import androidx.compose.ui.input.key.isMetaPressed
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import chirp.core.design_system.generated.resources.icon_cloud_off
import chirp.core.design_system.generated.resources.Res as DesignSystemRes
import chirp.feature.chat.presentation.generated.resources.Res
import chirp.feature.chat.presentation.generated.resources.send
import chirp.feature.chat.presentation.generated.resources.send_a_message
import com.jjasystems.chirp.chat.domain.model.ConnectionState
import com.jjasystems.chirp.chat.presentation.util.toUiText
import com.jjasystems.chirp.core.design_system.components.buttons.ChirpButton
import com.jjasystems.chirp.core.design_system.components.textfields.ChirpMultiLineTextField
import com.jjasystems.chirp.core.design_system.theme.ChirpTheme
import com.jjasystems.chirp.core.design_system.theme.extended
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun MessageBox(
    messageTextFieldState: TextFieldState,
    isSendButtonEnabled: Boolean,
    connectionState: ConnectionState,
    onSendClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isConnected = connectionState == ConnectionState.CONNECTED

    ChirpMultiLineTextField(
        state = messageTextFieldState,
        modifier = modifier
            .onPreviewKeyEvent { keyEvent ->
                val isModifierKeyPressed = keyEvent.isMetaPressed || keyEvent.isCtrlPressed
                val isSentShortcutPressed = isModifierKeyPressed
                        && keyEvent.key == Key.Enter
                        && keyEvent.type == KeyEventType.KeyDown

                if (isSentShortcutPressed) {
                    onSendClick()
                    true
                } else false
            },
        placeholder = stringResource(Res.string.send_a_message),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Send
        ),
        onKeyboardAction = {
            onSendClick()
        },
        bottomContent = {
            Spacer(modifier = Modifier.weight(1f))

            if(!isConnected) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = vectorResource(DesignSystemRes.drawable.icon_cloud_off),
                        contentDescription = connectionState.toUiText().asString(),
                        modifier = Modifier
                            .size(16.dp),
                        tint = MaterialTheme.colorScheme.extended.textDisabled
                    )

                    Text(
                        text = connectionState.toUiText().asString(),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.extended.textDisabled
                    )
                }
            }

            ChirpButton(
                text = stringResource(Res.string.send),
                onClick = onSendClick,
                enabled = isConnected && isSendButtonEnabled
            )
        }
    )
}

@Composable
@Preview
fun MessageBox_Preview() {
    ChirpTheme {
        MessageBox(
            messageTextFieldState = rememberTextFieldState(),
            isSendButtonEnabled = true,
            connectionState = ConnectionState.CONNECTED,
            onSendClick = {}
        )
    }
}