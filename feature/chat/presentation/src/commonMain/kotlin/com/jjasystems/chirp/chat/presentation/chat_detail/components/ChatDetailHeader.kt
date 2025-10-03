package com.jjasystems.chirp.chat.presentation.chat_detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import chirp.core.design_system.generated.resources.Res as DesignSystemRes
import chirp.core.design_system.generated.resources.icon_arrow_left
import chirp.core.design_system.generated.resources.icon_dots
import chirp.core.design_system.generated.resources.icon_log_out
import chirp.feature.chat.presentation.generated.resources.Res
import chirp.feature.chat.presentation.generated.resources.chat_members
import chirp.feature.chat.presentation.generated.resources.go_back
import chirp.feature.chat.presentation.generated.resources.icon_users
import chirp.feature.chat.presentation.generated.resources.leave_chat
import chirp.feature.chat.presentation.generated.resources.open_chat_options_menu
import com.jjasystems.chirp.chat.presentation.components.ChatHeader
import com.jjasystems.chirp.chat.presentation.components.ChatItemHeaderRow
import com.jjasystems.chirp.chat.presentation.model.ChatUiModel
import com.jjasystems.chirp.core.design_system.components.avatar.ChatParticipantUiModel
import com.jjasystems.chirp.core.design_system.components.buttons.ChirpIconButton
import com.jjasystems.chirp.core.design_system.components.dropdown.ChirpDropdownMenu
import com.jjasystems.chirp.core.design_system.components.dropdown.ChirpDropdownMenuItemUiModel
import com.jjasystems.chirp.core.design_system.theme.ChirpTheme
import com.jjasystems.chirp.core.design_system.theme.extended
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ChatDetailHeader(
    chat: ChatUiModel?,
    isDetailPresent: Boolean,
    isChatDropdownOpen: Boolean,
    onChatOptionsClick: () -> Unit,
    onDismissChatOptions: () -> Unit,
    onManageChatClick: () -> Unit,
    onLeaveChatClick: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row (
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (!isDetailPresent) {
            ChirpIconButton(
                onClick = onBackClick
            ) {
                Icon(
                    imageVector = vectorResource(DesignSystemRes.drawable.icon_arrow_left),
                    contentDescription = stringResource(Res.string.go_back),
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.extended.textSecondary
                )
            }
        }

        chat?.let {
            val isGroupChat = chat.otherParticipants.size > 1

            ChatItemHeaderRow(
                chat = chat,
                isGroupChat = isGroupChat,
                modifier = Modifier
                    .weight(1f)
                    .clickable {
                        onManageChatClick()
                    }
            )
        } ?: Spacer(modifier = Modifier.weight(1f))

        Box {
            ChirpIconButton(
                onClick = onChatOptionsClick
            ) {
                Icon(
                    imageVector = vectorResource(DesignSystemRes.drawable.icon_dots),
                    contentDescription = stringResource(Res.string.open_chat_options_menu),
                    modifier = Modifier.size(20.dp),
                    tint = MaterialTheme.colorScheme.extended.textSecondary
                )
            }

            ChirpDropdownMenu(
                isOpen = isChatDropdownOpen,
                onDismiss = onDismissChatOptions,
                menuItems = listOf(
                    ChirpDropdownMenuItemUiModel(
                        title = stringResource(Res.string.chat_members),
                        icon = vectorResource(Res.drawable.icon_users),
                        contentColor = MaterialTheme.colorScheme.extended.textSecondary,
                        onClick = onManageChatClick
                    ),
                    ChirpDropdownMenuItemUiModel(
                        title = stringResource(Res.string.leave_chat),
                        icon = vectorResource(DesignSystemRes.drawable.icon_log_out),
                        contentColor = MaterialTheme.colorScheme.extended.destructiveHover,
                        onClick = onLeaveChatClick
                    )
                )
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun ChatDetailHeader_Preview() {
    ChirpTheme {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            ChatHeader {
                ChatDetailHeader(
                    chat = ChatUiModel(
                        id = "1",
                        localParticipant = ChatParticipantUiModel(
                            id = "1",
                            username = "Shagy",
                            initials = "SH"
                        ),
                        otherParticipants = listOf(
                            ChatParticipantUiModel(
                                id = "2",
                                username = "Philipp",
                                initials = "PH"
                            ),
                            ChatParticipantUiModel(
                                id = "3",
                                username = "Luchi",
                                initials = "LU"
                            )
                        ),
                        lastMessage = null,
                        lastMessageSenderUsername = null
                    ),
                    isDetailPresent = false,
                    isChatDropdownOpen = true,
                    onChatOptionsClick = {},
                    onManageChatClick = {},
                    onLeaveChatClick = {},
                    onDismissChatOptions = {},
                    onBackClick = {}
                )
            }
        }
    }
}