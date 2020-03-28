package com.persona5dex.repositories

import com.persona5dex.filterGameType
import com.persona5dex.models.GameType
import com.persona5dex.models.PersonaForFusionService
import com.persona5dex.models.room.PersonaDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PersonaFusionRepository @Inject constructor(
        personaDao: PersonaDao,
        private val gameType: GameType
) {
    private val allPersonas: List<PersonaForFusionService> = personaDao.personasByLevel
            .sortedBy { it.level }
            .toList()

    suspend fun getFusionPersonas() =
            withContext(Dispatchers.IO) {
                allPersonas.filterGameType(gameType)
            }

    companion object {
        internal const val DLC_SHARED_PREF = "pref_dlc_persona_owned"
    }
}
