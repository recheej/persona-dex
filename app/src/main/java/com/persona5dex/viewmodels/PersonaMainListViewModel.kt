package com.persona5dex.viewmodels

import androidx.lifecycle.*
import com.persona5dex.ArcanaNameProvider
import com.persona5dex.extensions.toLowerCaseInsensitive
import com.persona5dex.filterGameType
import com.persona5dex.models.Enumerations
import com.persona5dex.models.GameType
import com.persona5dex.models.MainListPersona
import com.persona5dex.models.PersonaFilterArgs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

/**
 * Created by Rechee on 11/18/2017.
 */
class PersonaMainListViewModel(private val arcanaNameProvider: ArcanaNameProvider, private val gameType: GameType) : ViewModel() {
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

    private val stateLiveData = MutableLiveData<State>()

    private val filteredLiveData = MediatorLiveData<List<MainListPersona>>()

    init {
        sortByPersonaNameAsc = Comparator { o1: MainListPersona, o2: MainListPersona -> o1.name.compareTo(o2.name) }
        sortByPersonaNameDesc = Comparator { o1: MainListPersona, o2: MainListPersona -> o1.name.compareTo(o2.name) * -1 }
        sortByPersonaLevelAsc = Comparator { o1: MainListPersona, o2: MainListPersona -> o1.level.compareTo(o2.level) }
        sortByPersonaLevelDesc = Comparator { o1: MainListPersona, o2: MainListPersona -> o1.level.compareTo(o2.level) * -1 }
        sortByPersonaArcanaAsc = Comparator { o1: MainListPersona, o2: MainListPersona -> getPersonaNameComparison(o1, o2) }
        sortByPersonaArcanaDesc = Comparator { o1: MainListPersona, o2: MainListPersona -> getPersonaNameComparison(o1, o2) * -1 }
    }

    fun getState(): LiveData<State> = stateLiveData

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
        filterPersonas(newFilterArgs)
    }

    fun getFilteredPersonas() = filteredLiveData

    fun getFilterArgs() = Transformations.map(personaFilterArgs) {
        it ?: PersonaFilterArgs()
    }

    fun getArcanaNamesForSpinner() = arcanaNameProvider.getAllArcanaNamesForDisplay()

    fun initialize(allPersonas: List<MainListPersona>) {
        if (!initialized) {

            filteredLiveData.addSource(personaSearchName) { searchValue: String? ->
                if (searchValue == null || searchValue.isEmpty()) {
                    filteredLiveData.setValue(allPersonas)
                } else {
                    val finalList: MutableList<MainListPersona> = ArrayList()
                    for (mainListPersona in allPersonas) {
                        val searchValueLower = searchValue.toLowerCaseInsensitive()
                        if (mainListPersona.name.toLowerCaseInsensitive().contains(searchValueLower)) {
                            finalList.add(mainListPersona)
                        } else {
                            for (personaShadowName in mainListPersona.personaShadowNames) {
                                if (personaShadowName.shadowName.toLowerCaseInsensitive().contains(searchValueLower)) {
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
                    lastPersonaComparator = if (asc != false) {
                        Collections.sort(newPersonas, sortByPersonaNameAsc)
                        sortByPersonaNameAsc
                    } else {
                        Collections.sort(newPersonas, sortByPersonaNameDesc)
                        sortByPersonaNameDesc
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
                    lastPersonaComparator = if (asc != false) {
                        Collections.sort(newPersonas, sortByPersonaLevelAsc)
                        sortByPersonaLevelAsc
                    } else {
                        Collections.sort(newPersonas, sortByPersonaLevelDesc)
                        sortByPersonaLevelDesc
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
                    lastPersonaComparator = if (asc != false) {
                        Collections.sort(newPersonas, sortByPersonaArcanaAsc)
                        sortByPersonaArcanaAsc
                    } else {
                        Collections.sort(newPersonas, sortByPersonaArcanaDesc)
                        sortByPersonaArcanaDesc
                    }
                }
                filteredLiveData.setValue(newPersonas)
            }
            filteredLiveData.addSource(personaFilterArgs) { inputFilterArgs: PersonaFilterArgs ->
                val finalValue: MutableList<MainListPersona> = ArrayList()
                val comparator = lastPersonaComparator ?: sortByPersonaNameAsc
                val newPersonas = allPersonas
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
                filteredLiveData.setValue(finalValue)
            }
            initialized = true

            filterPersonas(gameType)

            stateLiveData.postValue(State.InitializeLoading)
        }
    }

    sealed class State {
        object InitializeLoading: State()
    }

    private fun getPersonaNameComparison(personaOne: MainListPersona, personaTwo: MainListPersona) =
            arcanaNameProvider.getArcanaNameForDisplay(personaOne.arcana).compareTo(arcanaNameProvider.getArcanaNameForDisplay(personaTwo.arcana))
}