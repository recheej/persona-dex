package com.persona5dex

import com.facebook.stetho.Stetho

/**
 * Created by Rechee on 7/1/2017.
 */

class Persona5DebugApplication : Persona5Application() {
    override fun onCreate() {
        super.onCreate()

        Stetho.initializeWithDefaults(this)
    }
}