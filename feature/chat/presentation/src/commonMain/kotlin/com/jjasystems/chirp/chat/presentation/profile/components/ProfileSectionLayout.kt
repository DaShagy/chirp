package com.jjasystems.chirp.chat.presentation.profile.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jjasystems.chirp.core.design_system.theme.ChirpTheme
import com.jjasystems.chirp.core.design_system.theme.extended
import com.jjasystems.chirp.core.presentation.util.DeviceConfiguration
import com.jjasystems.chirp.core.presentation.util.currentDeviceConfiguration
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ProfileSectionLayout(
    headerText: String,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    val deviceConfiguration = currentDeviceConfiguration()

    when(deviceConfiguration) {
        DeviceConfiguration.MOBILE_PORTRAIT ->{
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.Start,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = headerText,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.extended.textTertiary
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    content = content
                )
            }
        }
        else -> {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.Top,
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = headerText,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.extended.textTertiary
                )

                Column(
                    modifier = Modifier
                        .weight(3f),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    content = content
                )
            }
        }
    }
}

@Composable
@Preview
fun ProfileSectionLayout_Preview() {
    ChirpTheme {
        ProfileSectionLayout(
            headerText = "Header Text",
        ) {
            Text("A content")
        }
    }
}