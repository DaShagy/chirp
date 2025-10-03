package com.jjasystems.chirp.chat.presentation.util

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.jjasystems.chirp.core.design_system.theme.extended

@Composable
fun getChatBubbleColorForUser(userId: String): Color {
    val colorPool = with(MaterialTheme.colorScheme.extended) {
        listOf(
            cakeRed,
            cakeBlue,
            cakeMint,
            cakePink,
            cakeTeal,
            cakeGreen,
            cakeOrange,
            cakePurple,
            cakeViolet,
            cakeYellow
        )
    }
    val index = userId.hashCode().toUInt() % colorPool.size.toUInt()

    return colorPool[index.toInt()]
}