package com.jjasystems.chirp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        var shouldShowSplashScreen = true

        installSplashScreen().setKeepOnScreenCondition {
            shouldShowSplashScreen
        }

        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            App(
                onAuthenticatedCheck = {
                    shouldShowSplashScreen = false
                }
            )
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App(
        onAuthenticatedCheck = {}
    )
}