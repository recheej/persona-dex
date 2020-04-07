package com.persona5dex.viewmodels

import android.content.SharedPreferences
import androidx.lifecycle.*
import com.persona5dex.ArcanaNameProvider
import com.persona5dex.SHARED_PREF_KEY_GAME_TYPE
import com.persona5dex.extensions.toLowerCaseInsensitive
import com.persona5dex.filterGameType
import com.persona5dex.models.*
import com.persona5dex.models.GameType.Companion.toGameType
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.*

/**
 * Created by Rechee on 11/18/2017.
 */
class PersonaMainListViewModel(
        private val arcanaNameProvider: ArcanaNameProvider,
        private var currentGameType: GameType,
        private val personaRepository: PersonaRepository,
        private val defaultSharedSharedPreferences: SharedPreferences
) : ViewModel(), SharedPreferences.OnSharedPreferenceChangeListener {
    private val personaSearchName: MutableLiveData<String> = MutableLiveData()
    private val personasByName: MutableLiveData<Boolean> = MutableLiveData()
    private val personasByLevel: MutableLiveData<Boolean> = MutableLiveData()
    private val personasByArcana: MutableLiveData<Boolean> = MutableLiveData()
    private val personaFilterArgs: MutableLiveData<PersonaFilterArgs> = MutableLiveData()
    private val stateLiveData = MutableLiveData<State>()
    private val gameTypeLiveData = MutableLiveData<GameType>()
    private val allPersonas by lazy {
        viewModelScope.async {
            personaRepository.getPersonas()
        }
    }

    private val filteredLiveData = MediatorLiveData<List<MainListPersona>>().apply {
        addSource(personaSearchName) { searchValue: String? ->
            viewModelScope.launch {
                try {
                    val allPersonas = getNewPersonaList(checkNotNull(personaFilterArgs.value))
                    if (searchValue.isNullOrEmpty()) {
                        setValue(allPersonas)
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
                        setValue(finalList)
                    }
                } finally {
                    finishLoading()
                }
            }
        }
        addSource(personasByName) { asc: Boolean? ->
            val newPersonas = value
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
            value = newPersonas
            finishLoading()
        }
        addSource(personasByLevel) { asc: Boolean? ->
            val newPersonas = value
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
            value = newPersonas
            finishLoading()
        }
        addSource(personasByArcana) { asc: Boolean? ->
            val newPersonas = value
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
            value = newPersonas
            finishLoading()
        }
        addSource(personaFilterArgs) { inputFilterArgs: PersonaFilterArgs ->
            val finalValue: MutableList<MainListPersona> = ArrayList()
            viewModelScope.launch {
                try {
                    val newPersonas = getNewPersonaList(inputFilterArgs)

                    for (persona in newPersonas) {
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
                    setValue(finalValue)
                } finally {
                    finishLoading()
                }
            }
        }
    }

    private suspend fun getNewPersonaList(inputFilterArgs: PersonaFilterArgs): List<MainListPersona> {
        val comparator = lastPersonaComparator ?: sortByPersonaNameAsc
        val allPersonas = if (allPersonas.isCompleted) allPersonas.getCompleted() else allPersonas.await()
        return allPersonas
                .filterGameType(currentGameType, inputFilterArgs.basePersonas, inputFilterArgs.royalPersonas)
                .sortedWith(comparator)
    }

    private val sortByPersonaNameDesc: Comparator<MainListPersona>
    private val sortByPersonaNameAsc: Comparator<MainListPersona>
    private val sortByPersonaLevelAsc: Comparator<MainListPersona>
    private val sortByPersonaLevelDesc: Comparator<MainListPersona>
    private val sortByPersonaArcanaAsc: Comparator<MainListPersona>
    private val sortByPersonaArcanaDesc: Comparator<MainListPersona>

    private var lastPersonaComparator: Comparator<MainListPersona>? = null

    init {
        sortByPersonaNameAsc = Comparator { o1: MainListPersona, o2: MainListPersona -> o1.name.compareTo(o2.name) }
        sortByPersonaNameDesc = Comparator { o1: MainListPersona, o2: MainListPersona -> o1.name.compareTo(o2.name) * -1 }
        sortByPersonaLevelAsc = Comparator { o1: MainListPersona, o2: MainListPersona -> o1.level.compareTo(o2.level) }
        sortByPersonaLevelDesc = Comparator { o1: MainListPersona, o2: MainListPersona -> o1.level.compareTo(o2.level) * -1 }
        sortByPersonaArcanaAsc = Comparator { o1: MainListPersona, o2: MainListPersona -> getPersonaNameComparison(o1, o2) }
        sortByPersonaArcanaDesc = Comparator { o1: MainListPersona, o2: MainListPersona -> getPersonaNameComparison(o1, o2) * -1 }

        personaFilterArgs.value = PersonaFilterArgs().apply {
            basePersonas = true
            royalPersonas = currentGameType == GameType.ROYAL
        }

        filterPersonas(currentGameType)
        stateLiveData.postValue(State.LoadingStarted)

        defaultSharedSharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    fun getState(): LiveData<State> = stateLiveData

    fun filterPersonas(personaNameQuery: String?) {
        startLoading()
        personaSearchName.postValue(personaNameQuery)
    }

    fun sortPersonasByName(asc: Boolean) {
        startLoading()
        personasByName.value = asc
    }

    fun sortPersonasByLevel(asc: Boolean) {
        startLoading()
        personasByLevel.value = asc
    }

    fun sortPersonasByArcana(asc: Boolean) {
        startLoading()
        personasByArcana.value = asc
    }

    fun filterPersonas(filterArgs: PersonaFilterArgs) {
        startLoading()
        personaFilterArgs.value = filterArgs
    }

    fun filterPersonas(gameType: GameType) {
        startLoading()
        var gamTypeChanged = false
        if (gameType != currentGameType) {
            gamTypeChanged = true
            gameTypeLiveData.value = gameType
        }
        currentGameType = gameType

        val newFilterArgs = checkNotNull(personaFilterArgs.value) {
            "not expecting filter args to be null. game type $gameType"
        }.let {
            if (gamTypeChanged) {
                if (gameType == GameType.BASE) {
                    it.basePersonas = true
                    it.royalPersonas = false
                } else {
                    it.basePersonas = true
                    it.royalPersonas = true
                }
            }
            it
        }

        filterPersonas(newFilterArgs)
    }

    fun getGameType() = gameTypeLiveData

    fun getFilteredPersonas() = filteredLiveData

    fun getFilterArgs() = personaFilterArgs

    fun getArcanaNamesForSpinner() = arcanaNameProvider.getAllArcanaNamesForDisplay()

    private fun startLoading() {
        stateLiveData.value = State.LoadingStarted
    }

    private fun finishLoading() {
        stateLiveData.value = State.LoadingCompleted
    }

    override fun onCleared() {
        super.onCleared()
        defaultSharedSharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    sealed class State {
        object LoadingStarted : State()
        object LoadingCompleted : State()
    }

    private fun getPersonaNameComparison(personaOne: MainListPersona, personaTwo: MainListPersona) =
            arcanaNameProvider.getArcanaNameForDisplay(personaOne.arcana).compareTo(arcanaNameProvider.getArcanaNameForDisplay(personaTwo.arcana))

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (key == SHARED_PREF_KEY_GAME_TYPE) {
            val newGameType = defaultSharedSharedPreferences.getInt(SHARED_PREF_KEY_GAME_TYPE, GameType.BASE.value).toGameType()
            if (newGameType != gameTypeLiveData.value) {
                gameTypeLiveData.value = newGameType
            }
        }
    }
}