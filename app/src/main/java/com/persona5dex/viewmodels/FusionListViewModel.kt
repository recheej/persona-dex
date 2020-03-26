package com.persona5dex.viewmodels

import androidx.lifecycle.*
import com.persona5dex.extensions.normalize
import com.persona5dex.models.PersonaEdgeDisplay
import com.persona5dex.models.SimplePersonaNameView
import com.persona5dex.repositories.MainPersonaRepository
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FusionListViewModel(
        private val isToList: Boolean,
        private val personaId: Int,
        private val mainPersonaRepository: MainPersonaRepository
) : ViewModel() {

    private var initialized: Boolean = false
    private var baseEdgeDisplays: List<PersonaEdgeDisplay>? = null

    private lateinit var personaNameMap: Deferred<Map<Int, SimplePersonaNameView>>

    private val suggestionPersonaLiveData = MutableLiveData<Int>()
    private val queryLiveData = MutableLiveData<String?>()

    val queryForDisplay = MutableLiveData<String>()

    private val filteredEdgeDisplayLiveData = MediatorLiveData<List<PersonaEdgeDisplay>>().apply {
        addSource(queryLiveData) {
            value = it?.let {
                val queryNormalized = it.normalize()

                baseEdgeDisplays?.filter { display ->
                    if (isToList) {
                        display.leftPersonaName containsNormalized queryNormalized ||
                                display.rightPersonaName containsNormalized queryNormalized
                    } else {
                        val flag = display.resultPersonaName containsNormalized queryNormalized
                        val otherPersonaName =
                                if (display.leftPersonaID == personaId) display.rightPersonaName
                                else display.leftPersonaName
                        flag || otherPersonaName containsNormalized queryNormalized
                    }
                }.orEmpty()
            } ?: baseEdgeDisplays.orEmpty()
        }

        addSource(suggestionPersonaLiveData) { personaId ->
            viewModelScope.launch(Dispatchers.IO) {
                val nameMap = if (personaNameMap.isCompleted) personaNameMap.getCompleted()
                else personaNameMap.await()

                nameMap[personaId]?.let {
                    queryLiveData.postValue(it.name)
                    queryForDisplay.postValue(it.name)
                }
            }
        }
    }

    fun initialize() {
        personaNameMap = viewModelScope.async {
            mainPersonaRepository.getAllSimpleNames().map {
                SimplePersonaNameView(it.id, it.name)
            }.associateBy { it.id }
        }
        initialized = true
    }

    fun getFilteredEdgeDisplayLiveData(): LiveData<List<PersonaEdgeDisplay>> = filteredEdgeDisplayLiveData

    private infix fun String?.containsNormalized(other: String) =
            this?.normalize()?.contains(other) ?: false

    fun setEdgeDisplays(displays: List<PersonaEdgeDisplay>) {
        check(initialized)
        baseEdgeDisplays = displays
    }

    fun setSearch(query: String?) {
        check(initialized)
        queryLiveData.value = query
    }

    fun setSuggestionSearch(suggestionPersonaId: Int) {
        check(initialized)
        suggestionPersonaLiveData.value = suggestionPersonaId
    }
}

class FusionListViewModelFactory(
        private val isToList: Boolean,
        private val personaId: Int,
        private val mainPersonaRepository: MainPersonaRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
            FusionListViewModel(isToList, personaId, mainPersonaRepository) as T
}