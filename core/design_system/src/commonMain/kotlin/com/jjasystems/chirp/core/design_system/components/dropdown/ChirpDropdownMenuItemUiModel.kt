package com.jjasystems.chirp.core.design_system.components.dropdown

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class ChirpDropdownMenuItemUiModel(
    val title: String,
    val contentColor: Color,
    val icon: ImageVector,
    val onClick: () -> Unit
)
