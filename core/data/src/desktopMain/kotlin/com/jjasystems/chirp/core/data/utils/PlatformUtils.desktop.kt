package com.jjasystems.chirp.core.data.utils

actual object PlatformUtils {
    actual fun getOSName(): String {
        return System.getProperty("os.name")
    }
}