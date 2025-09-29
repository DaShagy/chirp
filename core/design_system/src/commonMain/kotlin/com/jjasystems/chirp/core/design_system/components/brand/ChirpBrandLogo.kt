package com.jjasystems.chirp.core.design_system.components.brand

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import chirp.core.design_system.generated.resources.Res
import chirp.core.design_system.generated.resources.logo_chirp
import com.jjasystems.chirp.core.design_system.theme.ChirpTheme
import org.jetbrains.compose.resources.vectorResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ChirpBrandLogo(
    modifier: Modifier = Modifier
) {
    Icon(
        imageVector = vectorResource(Res.drawable.logo_chirp),
        contentDescription = null,
        tint = MaterialTheme.colorScheme.primary,
        modifier = modifier
    )
}

@Composable
@Preview
fun ChirpBrandLogo_Preview() {
    ChirpTheme {
        ChirpBrandLogo()
    }
}