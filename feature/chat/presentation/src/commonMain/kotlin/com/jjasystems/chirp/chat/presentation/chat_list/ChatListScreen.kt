package com.jjasystems.chirp.chat.presentation.chat_list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.serialization.Serializable
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ChatListScreenRoot(
    viewModel: ChatListViewModel = koinViewModel()
) {
    Box(
        modifier = Modifier.fillMaxSize().fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Text("Chat List screen")
    }
}

@Serializable
data object ChatListRoute
