package com.jjasystems.chirp

import com.jjasystems.chirp.chat.data.notification.IosDeviceTokenHolder

object IosDeviceTokenHolderBridge {
    fun updateToken(token: String) {
        IosDeviceTokenHolder.updateToken(token)
    }
}