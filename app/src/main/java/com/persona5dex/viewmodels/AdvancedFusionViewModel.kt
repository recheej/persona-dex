package com.persona5dex.viewmodels

import androidx.lifecycle.*
import com.persona5dex.extensions.equalNormalized
import com.persona5dex.extensions.normalize
import com.persona5dex.filterGameType
import com.persona5dex.fusionService.advanced.AdvancedPersonaService
import com.persona5dex.models.GameType
import com.persona5dex.models.MainListPersona
import com.persona5dex.repositories.MainPersonaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.yield

class AdvancedFusionViewModel(personaId: Int,
                              private val mainPersonaRepository: MainPersonaRepository,
                              private val advancedPersonaService: AdvancedPersonaService,
                              private val gameType: GameType
) : ViewModel() {
    val personaName = Transformations.map(mainPersonaRepository.getPersonaName(personaId)) {
        it.orEmpty()
    }

    val recipesForAdvancedPersona: LiveData<List<MainListPersona>> = Transformations.switchMap(personaName) { personaName ->
        liveData(Dispatchers.IO) {

            val personas = advancedPersonaService.getAdvancedPersonas().toList()
                    .firstOrNull { it.resultPersonaName equalNormalized personaName }
                    ?.let { advancedFusions ->
                        yield()
                        val nameMap = mainPersonaRepository.allPersonasForMainList
                                .filterGameType(gameType)
                                .associateBy { it.name.normalize() }
                        yield()
                        advancedFusions.sourcePersonaNames
                                .map { nameMap.getValue(it.normalize()) }
                                .sortedBy { it.name }
                    }.orEmpty()


            emit(personas)
        }
    }
}

class AdvancedFusionViewModelFactory(private val personaId: Int,
                                     private val mainPersonaRepository: MainPersonaRepository,
                                     private val advancedPersonaService: AdvancedPersonaService,
                                     private val gameType: GameType
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
            AdvancedFusionViewModel(personaId, mainPersonaRepository, advancedPersonaService, gameType) as T
}