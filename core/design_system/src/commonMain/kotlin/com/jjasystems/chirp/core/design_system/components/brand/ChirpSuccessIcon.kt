package com.jjasystems.chirp.core.design_system.components.brand

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import chirp.core.design_system.generated.resources.Res
import chirp.core.design_system.generated.resources.success_checkmark
import com.jjasystems.chirp.core.design_system.theme.ChirpTheme
import com.jjasystems.chirp.core.design_system.theme.extended
import org.jetbrains.compose.resources.vectorResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ChirpSuccessIcon(
    modifier: Modifier = Modifier
) {
    Icon(
        imageVector = vectorResource(Res.drawable.success_checkmark),
        contentDescription = null,
        tint = MaterialTheme.colorScheme.extended.success,
        modifier = modifier
    )
}

@Composable
@Preview
fun ChirpSuccessIcon_Preview() {
    ChirpTheme {
        ChirpSuccessIcon()
    }
}