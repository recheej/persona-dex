package com.persona5dex.viewmodels

import androidx.lifecycle.*
import com.persona5dex.extensions.normalize
import com.persona5dex.models.PersonaEdgeDisplay

class FusionListViewModel(private val isToList: Boolean, private val personaId: Int) : ViewModel() {

    private val suggestionPersonaLiveData = MutableLiveData<Int>()
    private var baseEdgeDisplays: List<PersonaEdgeDisplay>? = null
    private val queryLiveData = MutableLiveData<String?>()

    private val filteredEdgeDisplayLiveData = MediatorLiveData<List<PersonaEdgeDisplay>>().apply {
        addSource(queryLiveData) {

        }
    }
//            Transformations.map(queryLiveData) {
//                it?.let {
//                    val queryNormalized = it.normalize()
//
//                    baseEdgeDisplays?.filter { display ->
//                        if (isToList) {
//                            display.leftPersonaName containsNormalized queryNormalized ||
//                                    display.rightPersonaName containsNormalized queryNormalized
//                        } else {
//                            val flag = display.resultPersonaName containsNormalized queryNormalized
//                            val otherPersonaName =
//                                    if (display.leftPersonaID == personaId) display.rightPersonaName
//                                    else display.leftPersonaName
//                            flag || otherPersonaName containsNormalized queryNormalized
//                        }
//                    }.orEmpty()
//                } ?: baseEdgeDisplays.orEmpty()
//            }

    fun getFilteredEdgeDisplayLiveData(): LiveData<List<PersonaEdgeDisplay>> = filteredEdgeDisplayLiveData

    private infix fun String?.containsNormalized(other: String) =
            this?.normalize()?.contains(other) ?: false

    fun setEdgeDisplays(displays: List<PersonaEdgeDisplay>) {
        baseEdgeDisplays = displays
    }

    fun setSearch(query: String?) {
        queryLiveData.value = query
    }

    fun setSuggestionSearch(suggestionPersonaId: Int) {
        suggestionPersonaLiveData.value = suggestionPersonaId
    }
}

class FusionListViewModelFactory(private val isToList: Boolean, private val personaId: Int) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
            FusionListViewModel(isToList, personaId) as T
}