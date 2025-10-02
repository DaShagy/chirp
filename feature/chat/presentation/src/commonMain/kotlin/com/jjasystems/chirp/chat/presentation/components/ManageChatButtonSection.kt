package com.jjasystems.chirp.chat.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.jjasystems.chirp.core.presentation.util.UiText

@Composable
fun ManageChatButtonSection(
    primaryButton: @Composable () -> Unit,
    secondaryButton: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    error: String? = null
) {
    Column(
        modifier = modifier
            .padding(16.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp, alignment = Alignment.End)
        ) {
            secondaryButton()
            primaryButton()
        }

        AnimatedVisibility(
            visible = error != null
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            error?.let {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = it,
                    textAlign = TextAlign.End,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    }

}