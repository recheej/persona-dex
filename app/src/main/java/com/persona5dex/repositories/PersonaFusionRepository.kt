package com.persona5dex.repositories

import android.content.SharedPreferences
import com.persona5dex.equalNormalized
import com.persona5dex.models.GameType
import com.persona5dex.models.PersonaForFusionService
import com.persona5dex.models.room.PersonaDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

class PersonaFusionRepository @Inject constructor(
        private val personaDao: PersonaDao,
        @Named("defaultSharedPreferences") private val defaultSharedPreferences: SharedPreferences,
        private val gameType: GameType
) {

    suspend fun getFusionPersonas() =
            withContext(Dispatchers.IO) {
                val personasByLevel = personaDao.personasByLevel
                val ownedDLCPersonas = defaultSharedPreferences.getStringSet(DLC_SHARED_PREF, emptySet<String>()).orEmpty()
                        .map { it.toInt() }
                        .mapNotNull { dlcId -> personasByLevel.firstOrNull { persona -> persona.id == dlcId } }

                val allPersonas: List<PersonaForFusionService> = personasByLevel
                        .filterNot { it.isRare || it.isSpecial || (it.isDlc && !ownedDLCPersonas.contains(it)) }
                        .sortedBy { it.level }
                        .toList()

                val basePersonas = allPersonas.filter { it.gameType == GameType.BASE }
                val filteredPersonas = if (gameType == GameType.BASE) {
                    basePersonas
                } else {
                    val personasForGameType = allPersonas.filter { it.gameType == gameType }
                    val allGameTypePersonas = basePersonas + personasForGameType
                    allGameTypePersonas
                            .filterNot { it.gameType == GameType.BASE && personasForGameType.any { gameTypePersona -> gameTypePersona.name equalNormalized it.name } }
                }

                filteredPersonas
            }

    companion object {
        internal const val DLC_SHARED_PREF = "pref_dlc_persona_owned"
    }
}
