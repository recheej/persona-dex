package com.persona5dex.repositories

import android.content.SharedPreferences
import com.persona5dex.models.room.PersonaDatabase
import javax.inject.Inject
import javax.inject.Named

class PersonaFusionRepository @Inject constructor(
        val database: PersonaDatabase,
        @Named val dlcSharedPreferences: SharedPreferences
)