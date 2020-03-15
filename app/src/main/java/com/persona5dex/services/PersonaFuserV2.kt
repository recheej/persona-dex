package com.persona5dex.services

import com.persona5dex.fusionService.FusionChart
import com.persona5dex.models.PersonaForFusionService
import com.persona5dex.repositories.PersonaFusionRepository
import javax.inject.Inject
import kotlin.math.floor

/**
 * Class that fuses two personas
 * fusion theory according to this: http://persona4.wikidot.com/fusiontutor
 * https://github.com/chinhodado/persona5_calculator/blob/master/src/FusionCalculator.js
 * https://www.gamefaqs.com/ps2/932312-shin-megami-tensei-persona-3/faqs/49926
 */
class PersonaFuserV2 @Inject constructor(
        private val fusionRepository: PersonaFusionRepository,
        private val fusionChart: FusionChart
) {

    suspend fun fusePersona(personaOne: PersonaForFusionService, personaTwo: PersonaForFusionService): PersonaForFusionService? {
        val personasByArcana = fusionRepository.getFusionPersonas().groupBy { it.arcana }

        return when {
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
    }

    private fun fuseSameArcanaPersonas(personaOne: PersonaForFusionService, personaTwo: PersonaForFusionService, rank: Int, personasInArcana: List<IndexedValue<PersonaForFusionService>>): PersonaForFusionService? =
            personasInArcana.lastOrNull {
                it.value.level < rank && it.value != personaOne && it.value != personaTwo
            }?.value
}