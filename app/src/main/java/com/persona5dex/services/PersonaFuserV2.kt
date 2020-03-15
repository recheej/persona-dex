package com.persona5dex.services

import com.persona5dex.fusionService.FusionChart
import com.persona5dex.models.PersonaForFusionService
import com.persona5dex.repositories.PersonaFusions
import javax.inject.Inject
import kotlin.math.floor

/**
 * Class that fuses two personas
 * fusion theory according to this: http://persona4.wikidot.com/fusiontutor
 * https://github.com/chinhodado/persona5_calculator/blob/master/src/FusionCalculator.js
 * https://www.gamefaqs.com/ps2/932312-shin-megami-tensei-persona-3/faqs/49926
 */
class PersonaFuserV2 @Inject constructor(
        private val personaFusions: PersonaFusions,
        private val fusionChart: FusionChart
) {
    private val personasByArcana by lazy {
        personaFusions.allPersonas
                .filterNot { it.isRare }
                .sortedBy { it.level }
                .groupBy { it.arcana }
    }

    fun fusePersona(personaOne: PersonaForFusionService, personaTwo: PersonaForFusionService): PersonaForFusionService? =
            when {
                personaOne == personaTwo -> null
                else -> {
                    val resultArcana = fusionChart.getResultArcana(personaOne.arcana, personaTwo.arcana)
                    resultArcana?.let {
                        val rank = floor(((personaOne.level + personaTwo.level) / 2).toDouble()).toInt() + 1

                        val personasInArcana = checkNotNull(personasByArcana[resultArcana]).withIndex().toList()

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