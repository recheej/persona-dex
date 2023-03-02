package com.persona5dex.viewmodels

import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.persona5dex.repositories.MainPersonaRepository

class AdvancedFusionViewModel(
    personaId: Int,
    mainPersonaRepository: MainPersonaRepository
) : ViewModel() {
    val personaName = Transformations.map(mainPersonaRepository.getPersonaName(personaId)) {
        it.orEmpty()
    }
}

class AdvancedFusionViewModelFactory(
    private val personaId: Int,
    private val mainPersonaRepository: MainPersonaRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        AdvancedFusionViewModel(personaId, mainPersonaRepository) as T
}