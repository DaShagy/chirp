package com.jjasystems.chirp.core.data.auth

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.jjasystems.chirp.core.data.dto.AuthInfoSerializable
import com.jjasystems.chirp.core.data.mapper.toDomain
import com.jjasystems.chirp.core.data.mapper.toSerializable
import com.jjasystems.chirp.core.domain.auth.AuthInfo
import com.jjasystems.chirp.core.domain.auth.SessionStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json

class DataStoreSessionStorage(
    private val dataStore: DataStore<Preferences>
): SessionStorage {

    private val authInfoKey = stringPreferencesKey("KEY_AUTH_INFO")

    private val json = Json {
        ignoreUnknownKeys
    }

    override fun observeAuthInfo(): Flow<AuthInfo?> {
        return dataStore.data.map { prefs ->
            val serializedJson = prefs[authInfoKey]
            serializedJson?.let {
                json.decodeFromString<AuthInfoSerializable>(it).toDomain()
            }
        }
    }

    override suspend fun set(info: AuthInfo?) {
        if (info == null) {
            dataStore.edit {
                it.remove(authInfoKey)
            }
            return
        }

        val serialized = json.encodeToString(info.toSerializable())
        dataStore.edit { prefs ->
            prefs[authInfoKey] = serialized
        }
    }
}