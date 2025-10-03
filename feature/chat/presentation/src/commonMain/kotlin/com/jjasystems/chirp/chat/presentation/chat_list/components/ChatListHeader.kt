package com.jjasystems.chirp.chat.presentation.chat_list.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import chirp.core.design_system.generated.resources.icon_log_out
import chirp.core.design_system.generated.resources.icon_users
import chirp.core.design_system.generated.resources.logo_chirp
import chirp.feature.chat.presentation.generated.resources.Res
import chirp.feature.chat.presentation.generated.resources.logout
import chirp.feature.chat.presentation.generated.resources.profile_settings
import com.jjasystems.chirp.chat.presentation.components.ChatHeader
import com.jjasystems.chirp.core.design_system.components.avatar.ChatParticipantUi
import com.jjasystems.chirp.core.design_system.components.avatar.ChirpAvatar
import com.jjasystems.chirp.core.design_system.components.brand.ChirpHorizontalDivider
import com.jjasystems.chirp.core.design_system.theme.ChirpTheme
import com.jjasystems.chirp.core.design_system.theme.extended
import org.jetbrains.compose.resources.stringResource
import chirp.core.design_system.generated.resources.Res as DesignSystemRes
import org.jetbrains.compose.resources.vectorResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ChatListHeader(
    localParticipant: ChatParticipantUi?,
    isUserMenuOpen: Boolean,
    onUserAvatarClick: () -> Unit,
    onDismissMenu: () -> Unit,
    onProfileSettingsClick: () -> Unit,
    onLogoutClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ChatHeader(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = vectorResource(DesignSystemRes.drawable.logo_chirp),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.tertiary
            )

            Text(
                text = "Chirp",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.extended.textPrimary
            )

            Spacer(modifier = Modifier.weight(1f))

            ProfileAvatarSection(
                localParticipant = localParticipant,
                isMenuOpen = isUserMenuOpen,
                onClick = onUserAvatarClick,
                onDismissMenu = onDismissMenu,
                onProfileSettingsClick = onProfileSettingsClick,
                onLogoutClick = onLogoutClick
            )
        }
    }
}

@Composable
fun ProfileAvatarSection(
    localParticipant: ChatParticipantUi?,
    isMenuOpen: Boolean,
    onClick: () -> Unit,
    onDismissMenu: () -> Unit,
    onProfileSettingsClick: () -> Unit,
    onLogoutClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
    ) {
        localParticipant?.let {
            ChirpAvatar(
                displayText = localParticipant.initials,
                imageUrl = localParticipant.imageUrl,
                onClick = onClick
            )
        }

        DropdownMenu(
            expanded = isMenuOpen,
            shape = RoundedCornerShape(16.dp),
            onDismissRequest = onDismissMenu,
            containerColor = MaterialTheme.colorScheme.surface,
            border = BorderStroke(
                width = 1.dp,
                color = MaterialTheme.colorScheme.extended.surfaceOutline
            )
        ) {
            DropdownMenuItem(
                 text = {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            imageVector = vectorResource(DesignSystemRes.drawable.icon_users),
                            contentDescription = stringResource(Res.string.profile_settings),
                            tint = MaterialTheme.colorScheme.extended.textSecondary,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = stringResource(Res.string.profile_settings),
                            color = MaterialTheme.colorScheme.extended.textSecondary,
                            fontWeight = FontWeight.Medium
                        )
                    }
                },
                onClick = {
                    onDismissMenu()
                    onProfileSettingsClick()
                }
            )

            ChirpHorizontalDivider()

            DropdownMenuItem(
                text = {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            imageVector = vectorResource(DesignSystemRes.drawable.icon_log_out),
                            contentDescription = stringResource(Res.string.logout),
                            tint = MaterialTheme.colorScheme.extended.destructiveHover,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = stringResource(Res.string.logout),
                            color = MaterialTheme.colorScheme.extended.destructiveHover,
                            fontWeight = FontWeight.Medium
                        )
                    }
                },
                onClick = {
                    onDismissMenu()
                    onLogoutClick()
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChatListHeader_Preview() {
    ChirpTheme {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            ChatListHeader(
                localParticipant = ChatParticipantUi(
                    id = "1",
                    username = "Juan Jose",
                    initials = "JJ",
                ),
                isUserMenuOpen = true,
                onDismissMenu = {},
                onLogoutClick = {},
                onUserAvatarClick = {},
                onProfileSettingsClick = {}
            )
        }
    }
}