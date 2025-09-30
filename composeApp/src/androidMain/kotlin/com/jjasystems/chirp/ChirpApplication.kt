package com.jjasystems.chirp

import android.app.Application
import com.jjasystems.chirp.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

class ChirpApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@ChirpApplication)
            androidLogger()
        }
    }
}