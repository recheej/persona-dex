package com.persona5dex

import android.app.Activity
import android.app.Application
import androidx.core.app.JobIntentService
import androidx.work.Configuration
import com.persona5dex.dagger.application.ApplicationContextModule
import com.persona5dex.dagger.application.DaggerPersona5ApplicationComponent
import com.persona5dex.dagger.application.Persona5ApplicationComponent
import com.persona5dex.models.room.PersonaDatabase


/**
 * Created by Rechee on 7/1/2017.
 */

open class Persona5Application : Application(), Configuration.Provider {
    lateinit var component: Persona5ApplicationComponent
        private set
    val database: PersonaDatabase by lazy { PersonaDatabase.getPersonaDatabase(this) }

    override fun onCreate() {
        super.onCreate()

        component = DaggerPersona5ApplicationComponent.builder()
            .applicationContextModule(ApplicationContextModule(this))
            .build()

        component.personaJobCreator().scheduleGenerateFusionJob()
    }

    override fun getWorkManagerConfiguration(): Configuration =
        Configuration.Builder().build()

    companion object {

        @JvmStatic
        fun get(activity: Activity): Persona5Application =
            activity.application as Persona5Application

        @JvmStatic
        fun get(service: JobIntentService): Persona5Application =
            service.application as Persona5Application
    }
}

