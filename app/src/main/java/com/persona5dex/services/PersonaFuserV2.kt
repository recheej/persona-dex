package com.persona5dex.services

import com.persona5dex.fusionService.FusionChart
import com.persona5dex.models.Enumerations
import com.persona5dex.models.PersonaForFusionService
import com.persona5dex.repositories.PersonaFusions
import javax.inject.Inject
import kotlin.math.floor

class PersonaFuserV2 @Inject constructor(
        private val personaFusions: PersonaFusions,
        private val fusionChart: FusionChart
) {
    private val personasByArcana: Map<Enumerations.Arcana, List<IndexedValue<PersonaForFusionService>>> by lazy {
        personaFusions.allPersonas
                .filterNot { it.isRare }
                .sortedBy { it.level }
                .withIndex().groupBy { it.value.arcana }
    }

    fun fusePersona(personaOne: PersonaForFusionService, personaTwo: PersonaForFusionService): PersonaForFusionService? =
            when {
                personaOne == personaTwo -> null
                else -> {
                    val resultArcana = fusionChart.getResultArcana(personaOne.arcana, personaTwo.arcana)
                    resultArcana?.let {
                        val rank = floor(((personaOne.level + personaTwo.level) / 2).toDouble()).toInt() + 1

                        val personasInArcana = checkNotNull(personasByArcana[resultArcana])

                        if (personaOne.arcana == personaTwo.arcana) {
                            return fuseSameArcanaPersonas(personaOne, personaTwo, rank, personasInArcana)
                        }

                        personasInArcana.firstOrNull { it.value.level >= rank }?.value
                    }
                }
            }

    private fun fuseSameArcanaPersonas(personaOne: PersonaForFusionService, personaTwo: PersonaForFusionService, rank: Int, personasInArcana: List<IndexedValue<PersonaForFusionService>>): PersonaForFusionService? =
            personasInArcana.lastOrNull {
                it.value.level < rank
            }?.let {
                return if (it.value == personaOne || it.value == personaTwo) {
                    personasInArcana.getOrNull(it.index - 1)?.value
                } else it.value
            }
}