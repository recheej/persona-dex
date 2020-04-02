package com.persona5dex.repositories

import com.crashlytics.android.Crashlytics
import com.persona5dex.dagger.activity.ActivityScope
import com.persona5dex.extensions.equalNormalized
import com.persona5dex.extensions.normalize
import com.persona5dex.filterGameType
import com.persona5dex.fusionService.advanced.AdvancedPersonaService
import com.persona5dex.models.GameType
import com.persona5dex.models.MainListPersona
import com.persona5dex.models.PersonaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

@ActivityScope
class AdvancedPersonaListRepository @Inject constructor(
        @Named("advancedPersonaId") private val advancedPersonaId: Int?,
        private val advancedPersonaService: AdvancedPersonaService,
        private val gameType: GameType,
        private val mainPersonaRepository: MainPersonaRepository
) : PersonaRepository {
    private val allPersonas by lazy {
        mainPersonaRepository.allPersonasForMainList
                .filterGameType(gameType)
    }

    private val nameMap by lazy {
        allPersonas.associateBy { it.name.normalize() }
    }

    override suspend fun getPersonas(): List<MainListPersona> = withContext(Dispatchers.IO) {
        try {
            val personaName = allPersonas.first { it.id == advancedPersonaId }
            advancedPersonaService.getAdvancedPersonas().toList()
                    .firstOrNull { it.resultPersonaName equalNormalized personaName.name }
                    ?.let { advancedFusions ->
                        advancedFusions.sourcePersonaNames
                                .map { nameMap.getValue(it.normalize()) }
                                .sortedBy { it.name }
                    }.orEmpty()
        } catch (e: NoSuchElementException) {
            Crashlytics.logException(RuntimeException("""
                failed to get advanced personas for persona: $advancedPersonaId
                game type: $gameType
                all personas count: $allPersonas
            """.trimIndent(), e))
            emptyList()
        }
    }
}