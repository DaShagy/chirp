package com.jjasystems.chirp.chat.presentation.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.jjasystems.chirp.core.design_system.components.avatar.ChatParticipantUiModel
import com.jjasystems.chirp.core.design_system.components.avatar.ChirpAvatar
import com.jjasystems.chirp.core.design_system.theme.extended
import com.jjasystems.chirp.core.design_system.theme.titleXSmall
import com.jjasystems.chirp.core.presentation.util.DeviceConfiguration
import com.jjasystems.chirp.core.presentation.util.currentDeviceConfiguration

@Composable
fun ColumnScope.ChatParticipantsSelectionSection(
    selectedParticipants: List<ChatParticipantUiModel>,
    modifier: Modifier = Modifier,
    searchResult: ChatParticipantUiModel? = null
) {
    val deviceConfiguration = currentDeviceConfiguration()
    val rootHeightModifier = when(deviceConfiguration) {
        DeviceConfiguration.TABLET_PORTRAIT,
        DeviceConfiguration.TABLET_LANDSCAPE,
        DeviceConfiguration.DESKTOP -> {
            Modifier
                .animateContentSize()
                .heightIn(min = 200.dp, max = 300.dp)
        }
        else -> Modifier.weight(1f)
    }

    Box(
        modifier = rootHeightModifier
            .then(modifier)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            searchResult?.let {
                item {
                    ChatParticipantListItem(
                        participantUi = searchResult,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            if(selectedParticipants.isNotEmpty() && searchResult == null) {
                items(
                    items = selectedParticipants,
                    key = { it.id }
                ) { selectedParticipant ->
                    ChatParticipantListItem(
                        participantUi = selectedParticipant,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
fun ChatParticipantListItem(
    participantUi: ChatParticipantUiModel,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        ChirpAvatar(
            displayText = participantUi.initials,
            imageUrl = participantUi.imageUrl
        )

        Text(
            text = participantUi.username,
            style = MaterialTheme.typography.titleXSmall,
            color = MaterialTheme.colorScheme.extended.textPrimary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}