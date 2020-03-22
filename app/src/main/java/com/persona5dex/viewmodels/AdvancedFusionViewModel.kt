package com.persona5dex.viewmodels

import androidx.lifecycle.*
import com.persona5dex.extensions.normalize
import com.persona5dex.fusionService.AdvancedPersonaFusionsFileService
import com.persona5dex.models.MainListPersona
import com.persona5dex.repositories.MainPersonaRepository
import kotlinx.coroutines.Dispatchers

class AdvancedFusionViewModelV2(personaId: Int,
                                private val mainPersonaRepository: MainPersonaRepository,
                                private val advancedPersonaFusionsFileService: AdvancedPersonaFusionsFileService
): ViewModel() {
    val personaName = Transformations.map(mainPersonaRepository.getPersonaName(personaId)) {
        it.orEmpty()
    }

    val recipesForAdvancedPersona: LiveData<List<MainListPersona>> = Transformations.switchMap(personaName) { personaName ->
        liveData(Dispatchers.IO) {
            val nameNormalized = personaName.normalize()

            val advancedFusions = advancedPersonaFusionsFileService.parseFile().first { it.resultPersonaName.normalize() == nameNormalized }

            val nameMap = mainPersonaRepository.allPersonasForMainList.associateBy { it.name.normalize() }

            val personas = advancedFusions.sourcePersonaNames
                    .map { nameMap.getValue(it.normalize()) }
                    .sortedBy { it.name }

            emit(personas)
        }
    }
}

class AdvancedFusionViewModelFactory(private val personaId: Int,
                                     private val mainPersonaRepository: MainPersonaRepository,
                                     private val advancedPersonaFusionsFileService: AdvancedPersonaFusionsFileService
): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
            AdvancedFusionViewModelV2(personaId, mainPersonaRepository, advancedPersonaFusionsFileService) as T
}