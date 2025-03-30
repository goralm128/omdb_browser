package com.mayag.omdbbrowser

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class OMDBBrowserApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        // Initialize global dependencies
    }
}

