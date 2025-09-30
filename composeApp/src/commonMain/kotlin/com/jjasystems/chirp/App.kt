package com.jjasystems.chirp

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

import chirp.composeapp.generated.resources.Res
import chirp.composeapp.generated.resources.compose_multiplatform
import com.jjasystems.chirp.auth.presentation.register.RegisterAction
import com.jjasystems.chirp.auth.presentation.register.RegisterRoot
import com.jjasystems.chirp.core.design_system.theme.ChirpTheme
import com.jjasystems.chirp.navigation.DeepLinkListener
import com.jjasystems.chirp.navigation.NavigationRoot

@Composable
@Preview
fun App() {
    val navController = rememberNavController()
    DeepLinkListener(navController)

    ChirpTheme {
        NavigationRoot(navController)
    }
}