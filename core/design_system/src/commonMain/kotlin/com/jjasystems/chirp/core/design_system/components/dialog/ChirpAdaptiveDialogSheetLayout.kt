package com.jjasystems.chirp.core.design_system.components.dialog

import androidx.compose.runtime.Composable
import com.jjasystems.chirp.core.presentation.util.currentDeviceConfiguration

@Composable
fun ChirpAdaptiveDialogSheetLayout(
    onDismiss: () -> Unit,
    content: @Composable () -> Unit
) {
    val configuration = currentDeviceConfiguration()

    if(configuration.isMobile) {
        ChirpBottomSheet(
            onDismiss = onDismiss,
            content = content
        )
    } else {
        ChirpDialog(
            onDismiss = onDismiss,
            content = content
        )
    }
}