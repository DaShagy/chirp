package com.jjasystems.chirp.deeplink

import com.jjasystems.chirp.navigation.ExternalUriHandler
import java.awt.Desktop
import javax.swing.SwingUtilities

object DesktopDeeplinkHandler {

    val supportedUriPatterns = listOf(
        Regex("^chirp://.*"),
        Regex("^https?://chirp\\.jja-systems\\.site/.*")
    )

    private var isInitialized = false

    fun setup() {
        if(isInitialized) {
            return
        }

        if(!Desktop.isDesktopSupported()) {
            return
        }

        try {
            val desktop = Desktop.getDesktop()

            if(desktop.isSupported(Desktop.Action.APP_OPEN_URI)) {

                desktop.setOpenURIHandler { event ->
                    val uri = event.uri.toString()
                    SwingUtilities.invokeLater {
                        processUri(uri)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun processUri(uri: String) {
        val cleanUri = uri.trim('"',' ')

        if (!isValidUri(cleanUri)) return

        ExternalUriHandler.onNewUri(cleanUri)
    }

    private fun isValidUri(uri: String): Boolean {
        return supportedUriPatterns.any { it.matches(uri) }
    }
}