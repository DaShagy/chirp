package com.jjasystems.chirp.core.design_system.components.avatar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.jjasystems.chirp.core.design_system.theme.ChirpTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ChirpStackedAvatars(
    avatars: List<AvatarUi>,
    modifier: Modifier = Modifier,
    size: AvatarSize = AvatarSize.SMALL,
    maxVisible: Int = 2,
    overlapPercentage: Float = 0.4f
) {
    val overlapOffset = -(size.dp * overlapPercentage)

    val visibleAvatars = avatars.take(maxVisible)
    val remainingCount = (avatars.size - maxVisible).coerceAtLeast(0)

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(overlapOffset),
        verticalAlignment = Alignment.CenterVertically
    ) {
        visibleAvatars.forEach { avatarUi ->
            ChirpAvatar(
                displayText = avatarUi.initials,
                size = size,
                imageUrl = avatarUi.imageUrl
            )
        }

        if(remainingCount > 0) {
            ChirpAvatar(
                displayText = "$remainingCount+",
                size = size,
                textColor = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
@Preview
fun ChirpStackedAvatars_Preview() {
    ChirpTheme {
        ChirpStackedAvatars(
            avatars = listOf(
                AvatarUi(
                    "1",
                    "JuanJo",
                    "JJ"
                ),
                AvatarUi(
                    "2",
                    "Shagy",
                    "SH"
                ),
                AvatarUi(
                    "3",
                    "Luchi",
                    "LU"
                ),
                AvatarUi(
                    "4",
                    "JuanJo",
                    "JJ"
                ),
            )
        )
    }
}