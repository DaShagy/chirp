package com.jjasystems.chirp.core.data.auth

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.jjasystems.chirp.core.data.utils.appDataDirectory
import java.io.File


fun createDataStore(): DataStore<Preferences> = createDataStore {
    val directory = appDataDirectory

    if (!directory.exists()) {
        directory.mkdirs()
    }

    File(directory, DATA_STORE_FILE_NAME).absolutePath
}