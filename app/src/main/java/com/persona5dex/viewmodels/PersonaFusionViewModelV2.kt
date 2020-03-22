package com.persona5dex.viewmodels

import androidx.lifecycle.*
import com.persona5dex.extensions.isDone
import com.persona5dex.jobs.PersonaJobCreator
import com.persona5dex.models.PersonaEdgeDisplay
import com.persona5dex.repositories.MainPersonaRepository
import com.persona5dex.repositories.PersonaDisplayEdgesRepository

class PersonaFusionViewModelV2(
        personaDisplayEdgesRepository: PersonaDisplayEdgesRepository,
        mainPersonaRepository: MainPersonaRepository,
        personaId: Int,
        personaJobCreator: PersonaJobCreator
) : ViewModel() {
    val personaIsAdvanced: LiveData<Int> = personaDisplayEdgesRepository.personaIsAdvanced(personaId)
    val personaName: LiveData<String> = mainPersonaRepository.getPersonaName(personaId)

    private val edgeComparator: Comparator<PersonaEdgeDisplay> = Comparator { o1, o2 ->
        if (o1.leftPersonaID == o2.leftPersonaID) {
            o1.rightPersonaName.compareTo(o2.rightPersonaName)
        } else o1.leftPersonaName.compareTo(o2.leftPersonaName)
    }

    val toEdges = Transformations.switchMap(personaIsAdvanced) {
        val isAdvanced = it == 1
        if (isAdvanced) {
            // no-op. there's no edges so don't update activity
            MutableLiveData<List<PersonaEdgeDisplay>>()
        } else {
            Transformations.switchMap(personaJobCreator.getStateForGenerateFusionJob()) { state ->
                if (state.isDone()) {
                    Transformations.map(personaDisplayEdgesRepository.getEdgesToPersona(personaId)) { edges ->
                        edges.sortEdges()
                    }
                } else MutableLiveData<List<PersonaEdgeDisplay>>()
            }
        }
    }

    val fromEdges =
            Transformations.switchMap(personaJobCreator.getStateForGenerateFusionJob()) { state ->
                if (state.isDone()) {
                    Transformations.map(personaDisplayEdgesRepository.getEdgesFromPersona(personaId)) {
                        it.forEach { display ->

                            //we want the left to be the persona that's not the current persona's
                            val left: String

                            val leftPersonaID: Int
                            val rightPersonaID: Int

                            if (display.leftPersonaID == personaId) {
                                left = display.rightPersonaName
                                leftPersonaID = display.rightPersonaID
                                rightPersonaID = display.resultPersonaID
                            } else {
                                left = display.leftPersonaName
                                leftPersonaID = display.leftPersonaID
                                rightPersonaID = display.rightPersonaID
                            }
                            val right: String = display.resultPersonaName

                            display.leftPersonaName = left
                            display.rightPersonaName = right
                            display.leftPersonaID = leftPersonaID
                            display.rightPersonaID = rightPersonaID
                        }
                        it.sortEdges()
                    }
                } else MutableLiveData<List<PersonaEdgeDisplay>>()
            }

    private fun List<PersonaEdgeDisplay>.sortEdges() = this.sortedWith(edgeComparator)
}

class PersonaFusionViewModelFactory(
        private val personaDisplayEdgesRepository: PersonaDisplayEdgesRepository,
        private val mainPersonaRepository: MainPersonaRepository,
        private val personaId: Int,
        private val personaJobCreator: PersonaJobCreator
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
            PersonaFusionViewModelV2(personaDisplayEdgesRepository, mainPersonaRepository, personaId, personaJobCreator) as T

}