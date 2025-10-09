package com.jjasystems.chirp.core.design_system.components.textfields

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import com.jjasystems.chirp.core.design_system.components.buttons.ChirpButton
import com.jjasystems.chirp.core.design_system.components.buttons.ChirpButtonStyle
import com.jjasystems.chirp.core.design_system.theme.ChirpTheme
import com.jjasystems.chirp.core.design_system.theme.extended
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ChirpMultiLineTextField(
    state: TextFieldState,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    enabled: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    onKeyboardAction: () -> Unit = {},
    maxHeightInLines: Int = 3,
    bottomContent: @Composable (RowScope.() -> Unit)? = null
) {
    val focusRequester = remember { FocusRequester() }

    Column(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.extended.surfaceLower,
                shape = RoundedCornerShape(8.dp)
            )
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.extended.surfaceOutline,
                shape = RoundedCornerShape(8.dp)
            )
            .clickable(
                interactionSource = null,
                indication = null,
                onClick = {
                    focusRequester.requestFocus()
                }
            )
            .padding(
                vertical = 12.dp,
                horizontal = 16.dp
            ),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        BasicTextField(
            state = state,
            enabled = enabled,
            modifier = Modifier.fillMaxWidth().focusRequester(focusRequester),
            textStyle = MaterialTheme.typography.bodyLarge.copy(
                color = MaterialTheme.colorScheme.extended.textPrimary
            ),
            lineLimits = TextFieldLineLimits.MultiLine(
                minHeightInLines = 1,
                maxHeightInLines = maxHeightInLines
            ),
            keyboardOptions = keyboardOptions,
            onKeyboardAction = { onKeyboardAction() },
            cursorBrush = SolidColor(MaterialTheme.colorScheme.extended.textPrimary),
            decorator = { innerBox ->
                if (placeholder != null && state.text.isEmpty()) {
                    Text(
                        text = placeholder,
                        color = MaterialTheme.colorScheme.extended.textPlaceholder,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                innerBox()
            }
        )

        bottomContent?.let {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                content = bottomContent
            )
        }
    }
}

@Composable
@Preview
fun ChirpMultiLineTextField_Preview() {
    ChirpTheme {
        ChirpMultiLineTextField(
            state = rememberTextFieldState(
                initialText =  "This is some text field content that maybe spans multiple lines"
            ),
            modifier = Modifier
                .widthIn(max = 300.dp)
                .heightIn(min = 100.dp),
            placeholder = null,
            maxHeightInLines = 3,
            bottomContent = {
                Spacer(modifier = Modifier.weight(1f))

                ChirpButton(
                    text = "Send",
                    onClick = {}
                )
            }
        )
    }
}