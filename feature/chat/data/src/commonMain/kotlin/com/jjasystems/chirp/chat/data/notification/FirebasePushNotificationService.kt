package com.jjasystems.chirp.chat.data.notification

import com.jjasystems.chirp.chat.domain.notification.PushNotificationService
import kotlinx.coroutines.flow.Flow

expect class FirebasePushNotificationService: PushNotificationService {
    override fun observeDeviceToken(): Flow<String?>
}