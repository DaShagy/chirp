package com.jjasystems.chirp.chat.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.jjasystems.chirp.chat.database.entity.ChatParticipantEntity

@Dao
interface ChatParticipantDao {

    @Upsert
    suspend fun upsertParticipant(participant: ChatParticipantEntity)

    @Upsert
    suspend fun upsertParticipants(participants: List<ChatParticipantEntity>)

    @Query("""
        UPDATE chatparticipantentity
        SET profilePictureUrl = :newUrl
        WHERE userId = :userId
    """)
    suspend fun updateProfilePictureUrl(userId: String, newUrl: String?)

    @Query("SELECT * FROM chatparticipantentity")
    suspend fun getAllParticipants(): List<ChatParticipantEntity>
}