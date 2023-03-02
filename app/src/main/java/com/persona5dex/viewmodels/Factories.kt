package com.persona5dex.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.persona5dex.ArcanaNameProvider
import com.persona5dex.repositories.PersonaDetailRepository
import com.persona5dex.repositories.PersonaElementsRepository

class PersonaDetailInfoViewModelFactory(
    private val repository: PersonaDetailRepository,
    private val arcanaNameProvider: ArcanaNameProvider,
    private val personaId: Int
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        PersonaDetailInfoViewModel(repository, arcanaNameProvider, personaId) as T
}

class PersonaElementsViewModelFactory(
    private val repository: PersonaElementsRepository,
    private val personaId: Int
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        PersonaElementsViewModel(repository, personaId) as T
}