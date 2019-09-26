package com.persona5dex

import android.app.Activity
import android.app.Application
import androidx.core.app.JobIntentService
import com.facebook.stetho.Stetho
import com.persona5dex.dagger.application.ApplicationContextModule
import com.persona5dex.dagger.application.DaggerPersona5ApplicationComponent
import com.persona5dex.dagger.application.Persona5ApplicationComponent
import com.persona5dex.models.room.PersonaDatabase

/**
 * Created by Rechee on 7/1/2017.
 */

open class Persona5Application : Application() {
    lateinit var component: Persona5ApplicationComponent
        private set
    val database: PersonaDatabase by lazy { PersonaDatabase.getPersonaDatabase(this) }

    override fun onCreate() {
        super.onCreate()

        component = DaggerPersona5ApplicationComponent.builder()
                .applicationContextModule(ApplicationContextModule(this))
                .build()
    }

    companion object {

        @JvmStatic fun get(activity: Activity): Persona5Application {
            return activity.application as Persona5Application
        }

        @JvmStatic fun get(service: JobIntentService): Persona5Application {
            return service.application as Persona5Application
        }
    }
}

/**
 * Created by Rechee on 7/1/2017.
 */

class Persona5DebugApplication : Persona5Application() {
    override fun onCreate() {
        super.onCreate()

        Stetho.initializeWithDefaults(this)
    }
}