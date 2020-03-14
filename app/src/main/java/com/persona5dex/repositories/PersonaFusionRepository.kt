package com.persona5dex.repositories

import android.content.Context
import android.content.SharedPreferences
import com.persona5dex.R
import com.persona5dex.models.GameType
import com.persona5dex.models.PersonaForFusionService
import com.persona5dex.models.room.PersonaDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

class PersonaFusionRepository @Inject constructor(
        database: PersonaDatabase,
        @Named("defaultSharedPreferences") private val defaultSharedPreferences: SharedPreferences,
        @Named("applicationContext") private val context: Context
) {
    private val personaDao = database.personaDao()
    private val dlcPrefKey = context.getString(R.string.pref_key_dlc)
    private val ownedDLCPersonaIds: Set<Int> =
            defaultSharedPreferences.getStringSet(dlcPrefKey, emptySet())
                    ?.map { it.toInt() }
                    ?.toSet()
                    .orEmpty()

    suspend fun getFusionPersonas(gameType: GameType) =
            withContext(Dispatchers.IO) {
                val allPersonas: List<PersonaForFusionService> = personaDao.getPersonasByLevel(gameType.value)
                        .toList()


                val ownedDLCPersonas = allPersonas
                        .filter { it.id in ownedDLCPersonaIds }.toList()
                PersonaFusions(allPersonas, ownedDLCPersonas)
            }
}

data class PersonaFusions(
        val allPersonas: List<PersonaForFusionService>,
        val ownedDLCPersonas: List<PersonaForFusionService>
)