package com.jjasystems.chirp.core.design_system.components.layouts

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.unit.dp
import chirp.core.design_system.generated.resources.Res
import chirp.core.design_system.generated.resources.logo_chirp
import com.jjasystems.chirp.core.design_system.theme.ChirpTheme
import org.jetbrains.compose.resources.vectorResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ChirpSurface(
    modifier: Modifier = Modifier,
    header: @Composable ColumnScope.() -> Unit = {},
    content: @Composable ColumnScope.() -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = modifier
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
        ) {
            header()
            Surface(
                color = MaterialTheme.colorScheme.surface,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(
                    topStart = 20.dp,
                    topEnd = 20.dp
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                        .verticalScroll(rememberScrollState()),
                    content = content
                )
            }
        }
    }
}

@Composable
@Preview
fun ChirpSurface_Preview() {
    ChirpTheme {
        ChirpSurface(
            modifier = Modifier
                .fillMaxSize(),
            header = {
                Icon(
                    imageVector = vectorResource(Res.drawable.logo_chirp),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .padding(vertical = 32.dp)
                )
            },
            content = {
                Text(
                    text = "Welcome to Chirp!",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier
                        .padding(vertical = 40.dp)
                        .align(Alignment.CenterHorizontally)
                )
            }
        )
    }
}