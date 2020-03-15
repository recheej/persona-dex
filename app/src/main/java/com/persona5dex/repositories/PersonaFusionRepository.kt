package com.persona5dex.repositories

import android.content.SharedPreferences
import com.persona5dex.R
import com.persona5dex.models.GameType
import com.persona5dex.models.PersonaForFusionService
import com.persona5dex.models.room.PersonaDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.yield
import javax.inject.Inject
import javax.inject.Named

class PersonaFusionRepository @Inject constructor(
        database: PersonaDatabase,
        private val ownedDLCPersonaIds: Set<Int>
) {
    private val personaDao = database.personaDao()

    suspend fun getFusionPersonas(gameType: GameType) =
            withContext(Dispatchers.IO) {
                val allPersonas: List<PersonaForFusionService> = personaDao.getPersonasByLevel(gameType.value)
                        .toList()
                yield()

                val ownedDLCPersonas = allPersonas
                        .filter { it.id in ownedDLCPersonaIds }.toSet()
                PersonaFusions(allPersonas, ownedDLCPersonas)
            }
}

data class PersonaFusions(
        val allPersonas: List<PersonaForFusionService>,
        val ownedDLCPersonas: Set<PersonaForFusionService>
)