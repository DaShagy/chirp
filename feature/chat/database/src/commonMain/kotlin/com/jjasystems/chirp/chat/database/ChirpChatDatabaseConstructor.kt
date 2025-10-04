package com.jjasystems.chirp.chat.database

import androidx.room.RoomDatabaseConstructor

@Suppress("KotlinNoActualForExpect", "NO_ACTUAL_FOR_EXPECT")
expect object ChirpChatDatabaseConstructor: RoomDatabaseConstructor<ChirpChatDatabase> {
    override fun initialize(): ChirpChatDatabase
}