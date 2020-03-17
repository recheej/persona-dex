package com.persona5dex.viewmodels

import androidx.lifecycle.*
import com.persona5dex.ArcanaNameProvider
import com.persona5dex.filterGameType
import com.persona5dex.models.Enumerations
import com.persona5dex.models.GameType
import com.persona5dex.models.MainListPersona
import com.persona5dex.models.PersonaFilterArgs
import com.persona5dex.repositories.MainPersonaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

/**
 * Created by Rechee on 11/18/2017.
 */
class PersonaMainListViewModel(private val repository: MainPersonaRepository, private val arcanaNameProvider: ArcanaNameProvider, private val gameType: GameType) : ViewModel() {
    private val personaSearchName: MutableLiveData<String> = MutableLiveData()
    private val personasByName: MutableLiveData<Boolean> = MutableLiveData()
    private val personasByLevel: MutableLiveData<Boolean> = MutableLiveData()
    private val personasByArcana: MutableLiveData<Boolean> = MutableLiveData()
    private val personaFilterArgs: MutableLiveData<PersonaFilterArgs> = MutableLiveData()
    private val sortByPersonaNameDesc: Comparator<MainListPersona>
    private val sortByPersonaNameAsc: Comparator<MainListPersona>
    private val sortByPersonaLevelAsc: Comparator<MainListPersona>
    private val sortByPersonaLevelDesc: Comparator<MainListPersona>
    private val sortByPersonaArcanaAsc: Comparator<MainListPersona>
    private val sortByPersonaArcanaDesc: Comparator<MainListPersona>
    private var lastPersonaComparator: Comparator<MainListPersona>? = null
    private var initialized: Boolean = false

    private val filteredLiveData = MediatorLiveData<List<MainListPersona>>()

    fun filterPersonas(personaNameQuery: String) {
        check(initialized)
        personaSearchName.postValue(personaNameQuery)
    }

    fun sortPersonasByName(asc: Boolean) {
        check(initialized)
        personasByName.value = asc
    }

    fun sortPersonasByLevel(asc: Boolean) {
        check(initialized)
        personasByLevel.value = asc
    }

    fun sortPersonasByArcana(asc: Boolean) {
        check(initialized)
        personasByArcana.value = asc
    }

    fun filterPersonas(filterArgs: PersonaFilterArgs) {
        check(initialized)
        personaFilterArgs.value = filterArgs
    }

    fun filterPersonas(gameType: GameType) {
        check(initialized)
        var newFilterArgs = personaFilterArgs.value
        if (newFilterArgs == null) {
            newFilterArgs = PersonaFilterArgs()
        }
        newFilterArgs.gameType = gameType
        personaFilterArgs.value = newFilterArgs
    }

    fun getFilteredPersonas() = filteredLiveData

    init {
        sortByPersonaNameAsc = Comparator { o1: MainListPersona, o2: MainListPersona -> o1.name.compareTo(o2.name) }
        sortByPersonaNameDesc = Comparator { o1: MainListPersona, o2: MainListPersona -> o1.name.compareTo(o2.name) * -1 }
        sortByPersonaLevelAsc = Comparator { o1: MainListPersona, o2: MainListPersona -> o1.level.compareTo(o2.level) }
        sortByPersonaLevelDesc = Comparator { o1: MainListPersona, o2: MainListPersona -> o1.level.compareTo(o2.level) * -1 }
        sortByPersonaArcanaAsc = Comparator { o1: MainListPersona, o2: MainListPersona -> getPersonaComparison(o1, o2) }
        sortByPersonaArcanaDesc = Comparator { o1: MainListPersona, o2: MainListPersona -> getPersonaComparison(o1, o2) * -1 }
    }

    fun initialize() {
        initialized = true
        viewModelScope.launch(Dispatchers.IO) {
            val personas = repository.allPersonasForMainList

            withContext(Dispatchers.Main) {
                filteredLiveData.addSource(personaSearchName) { searchValue: String? ->
                    if (searchValue == null || searchValue.isEmpty()) {
                        filteredLiveData.setValue(personas)
                    } else {
                        val finalList: MutableList<MainListPersona> = ArrayList()
                        for (mainListPersona in personas!!) {
                            val searchValueLower = searchValue.toLowerCase()
                            if (mainListPersona.name.toLowerCase().contains(searchValueLower)) {
                                finalList.add(mainListPersona)
                            } else {
                                for (personaShadowName in mainListPersona.personaShadowNames) {
                                    if (personaShadowName.shadowName.toLowerCase().contains(searchValueLower)) {
                                        finalList.add(mainListPersona)
                                        break
                                    }
                                }
                            }
                        }
                        filteredLiveData.setValue(finalList)
                    }
                }
                filteredLiveData.addSource(personasByName) { asc: Boolean? ->
                    val newPersonas = filteredLiveData.value
                    if (newPersonas != null) {
                        if (newPersonas.size == 1) {
                            return@addSource
                        }
                        if (asc != false) {
                            Collections.sort(newPersonas, sortByPersonaNameAsc)
                            lastPersonaComparator = sortByPersonaNameAsc
                        } else {
                            Collections.sort(newPersonas, sortByPersonaNameDesc)
                            lastPersonaComparator = sortByPersonaNameDesc
                        }
                    }
                    filteredLiveData.setValue(newPersonas)
                }
                filteredLiveData.addSource(personasByLevel) { asc: Boolean? ->
                    val newPersonas = filteredLiveData.value
                    if (newPersonas != null) {
                        if (newPersonas.size == 1) {
                            return@addSource
                        }
                        if (asc != false) {
                            Collections.sort(newPersonas, sortByPersonaLevelAsc)
                            lastPersonaComparator = sortByPersonaLevelAsc
                        } else {
                            Collections.sort(newPersonas, sortByPersonaLevelDesc)
                            lastPersonaComparator = sortByPersonaLevelDesc
                        }
                    }
                    filteredLiveData.setValue(newPersonas)
                }
                filteredLiveData.addSource(personasByArcana) { asc: Boolean? ->
                    val newPersonas = filteredLiveData.value
                    if (newPersonas != null) {
                        if (newPersonas.size == 1) {
                            return@addSource
                        }
                        if (asc != false) {
                            Collections.sort(newPersonas, sortByPersonaArcanaAsc)
                            lastPersonaComparator = sortByPersonaArcanaAsc
                        } else {
                            Collections.sort(newPersonas, sortByPersonaArcanaDesc)
                            lastPersonaComparator = sortByPersonaArcanaDesc
                        }
                    }
                    filteredLiveData.setValue(newPersonas)
                }
                filteredLiveData.addSource(personaFilterArgs) { inputFilterArgs: PersonaFilterArgs ->
                    val finalValue: MutableList<MainListPersona> = ArrayList()
                    if (personas != null) {
                        val comparator = lastPersonaComparator ?: sortByPersonaNameAsc
                        val newPersonas = personas
                                .filterGameType(inputFilterArgs.gameType)
                                .sortedWith(comparator)

                        for (persona in newPersonas) {
                            if (persona.rare && !inputFilterArgs.rarePersona) {
                                continue
                            }
                            if (persona.dlc && !inputFilterArgs.dlcPersona) {
                                continue
                            }
                            if (inputFilterArgs.arcana != Enumerations.Arcana.ANY && persona.arcana != inputFilterArgs.arcana) {
                                continue
                            }
                            if (persona.level < inputFilterArgs.minLevel || persona.level > inputFilterArgs.maxLevel) {
                                continue
                            }
                            finalValue.add(persona)
                        }
                    }
                    filteredLiveData.setValue(finalValue)
                }

                filterPersonas(gameType)
            }
        }
    }

    private fun getPersonaComparison(personaOne: MainListPersona, personaTwo: MainListPersona) =
            arcanaNameProvider.getArcanaNameForDisplay(personaOne.arcana).compareTo(arcanaNameProvider.getArcanaNameForDisplay(personaTwo.arcana))
}