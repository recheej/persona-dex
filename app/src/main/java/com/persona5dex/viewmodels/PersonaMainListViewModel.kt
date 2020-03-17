package com.persona5dex.viewmodels

import androidx.lifecycle.*
import com.persona5dex.ArcanaNameProvider
import com.persona5dex.filterGameType
import com.persona5dex.models.Enumerations
import com.persona5dex.models.GameType
import com.persona5dex.models.MainListPersona
import com.persona5dex.models.PersonaFilterArgs
import com.persona5dex.repositories.MainPersonaRepository
import java.util.*

/**
 * Created by Rechee on 11/18/2017.
 */
class PersonaMainListViewModel(private val repository: MainPersonaRepository, private val arcanaNameProvider: ArcanaNameProvider) : ViewModel() {
    private val personaSearchName: MutableLiveData<String>
    private val personasByName: MutableLiveData<Boolean>
    private val personasByLevel: MutableLiveData<Boolean>
    private val personasByArcana: MutableLiveData<Boolean>
    private val personaFilterArgs: MutableLiveData<PersonaFilterArgs>
    private val sortByPersonaNameDesc: Comparator<MainListPersona>
    private val sortByPersonaNameAsc: Comparator<MainListPersona>
    private val sortByPersonaLevelAsc: Comparator<MainListPersona>
    private val sortByPersonaLevelDesc: Comparator<MainListPersona>
    private val sortByPersonaArcanaAsc: Comparator<MainListPersona>
    private val sortByPersonaArcanaDesc: Comparator<MainListPersona>
    private val allPersonas: LiveData<List<MainListPersona>>
    val filteredPersonas: LiveData<List<MainListPersona>?>
    fun filterPersonas(personaNameQuery: String) {
        personaSearchName.postValue(personaNameQuery)
    }

    fun sortPersonasByName(asc: Boolean) {
        personasByName.value = asc
    }

    fun sortPersonasByLevel(asc: Boolean) {
        personasByLevel.value = asc
    }

    fun sortPersonasByArcana(asc: Boolean) {
        personasByArcana.value = asc
    }

    fun filterPersonas(filterArgs: PersonaFilterArgs) {
        personaFilterArgs.value = filterArgs
    }

    fun filterPersonas(gameType: GameType) {
        var newFilterArgs = personaFilterArgs.value
        if (newFilterArgs == null) {
            newFilterArgs = PersonaFilterArgs()
        }
        newFilterArgs.gameType = gameType
        personaFilterArgs.value = newFilterArgs
    }

    init {
        allPersonas = repository.allPersonasForMainList
        allPersonas.observeForever { mainListPersonas: List<MainListPersona>? -> }
        filteredPersonas = Transformations.switchMap(allPersonas) { personas: List<MainListPersona>? ->
            val filteredLiveData = MediatorLiveData<List<MainListPersona>?>()
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
                if (asc == null) {
                    asc = true
                }
                val newPersonas = filteredPersonas.value
                if (newPersonas != null) {
                    if (newPersonas.size == 1) {
                        return@addSource
                    }
                    if (asc) {
                        Collections.sort(newPersonas, sortByPersonaNameAsc)
                    } else {
                        Collections.sort(newPersonas, sortByPersonaNameDesc)
                    }
                }
                filteredLiveData.setValue(newPersonas)
            }
            filteredLiveData.addSource(personasByLevel) { asc: Boolean? ->
                if (asc == null) {
                    asc = true
                }
                val newPersonas = filteredPersonas.value
                if (newPersonas != null) {
                    if (newPersonas.size == 1) {
                        return@addSource
                    }
                    if (asc) {
                        Collections.sort(newPersonas, sortByPersonaLevelAsc)
                    } else {
                        Collections.sort(newPersonas, sortByPersonaLevelDesc)
                    }
                }
                filteredLiveData.setValue(newPersonas)
            }
            filteredLiveData.addSource(personasByArcana) { asc: Boolean? ->
                if (asc == null) {
                    asc = true
                }
                val newPersonas = filteredLiveData.value
                if (newPersonas != null) {
                    if (newPersonas.size == 1) {
                        return@addSource
                    }
                    if (asc) {
                        Collections.sort(newPersonas, sortByPersonaArcanaAsc)
                    } else {
                        Collections.sort(newPersonas, sortByPersonaArcanaDesc)
                    }
                }
                filteredLiveData.setValue(newPersonas)
            }
            filteredLiveData.addSource(personaFilterArgs) { inputFilterArgs: PersonaFilterArgs ->
                var filterArgs = inputFilterArgs
                if (filterArgs == null) {
                    filterArgs = PersonaFilterArgs()
                }
                val finalValue: MutableList<MainListPersona> = ArrayList()
                if (personas != null) {
                    val newPersonas = personas.filterGameType(inputFilterArgs.gameType)
                    for (persona in newPersonas) {
                        if (persona.rare && !filterArgs.rarePersona) {
                            continue
                        }
                        if (persona.dlc && !filterArgs.dlcPersona) {
                            continue
                        }
                        if (filterArgs.arcana != Enumerations.Arcana.ANY && persona.arcana != filterArgs.arcana) {
                            continue
                        }
                        if (persona.level < filterArgs.minLevel || persona.level > filterArgs.maxLevel) {
                            continue
                        }
                        finalValue.add(persona)
                    }
                }
                filteredLiveData.setValue(finalValue)
            }
            filteredLiveData
        }
        personaSearchName = MutableLiveData()
        personasByName = MutableLiveData()
        personasByLevel = MutableLiveData()
        personasByArcana = MutableLiveData()
        personaFilterArgs = MutableLiveData()
        sortByPersonaNameAsc = Comparator { o1: MainListPersona, o2: MainListPersona -> o1.name.compareTo(o2.name) }
        sortByPersonaNameDesc = Comparator { o1: MainListPersona, o2: MainListPersona -> o1.name.compareTo(o2.name) * -1 }
        sortByPersonaLevelAsc = Comparator { o1: MainListPersona, o2: MainListPersona -> Integer.compare(o1.level, o2.level) }
        sortByPersonaLevelDesc = Comparator { o1: MainListPersona, o2: MainListPersona -> Integer.compare(o1.level, o2.level) * -1 }
        sortByPersonaArcanaAsc = Comparator { o1: MainListPersona, o2: MainListPersona -> o1.arcana.getNameForDisplay().compareTo(o2.arcana.getNameForDisplay()) }
        sortByPersonaArcanaDesc = Comparator { o1: MainListPersona, o2: MainListPersona -> o1.arcana.getNameForDisplay().compareTo(o2.arcana.getNameForDisplay()) * -1 }
    }
}