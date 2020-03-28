package com.persona5dex.fusionService

import android.content.SharedPreferences
import com.persona5dex.models.PersonaForFusionService
import com.persona5dex.repositories.PersonaFusionRepository
import kotlin.math.floor

/**
 * Class that fuses two personas
 * fusion theory according to this: http://persona4.wikidot.com/fusiontutor
 * https://github.com/chinhodado/persona5_calculator/blob/master/src/FusionCalculator.js
 * https://www.gamefaqs.com/ps2/932312-shin-megami-tensei-persona-3/faqs/49926
 */
class PersonaFuserV2(
        private val fusionRepository: PersonaFusionRepository,
        private val fusionChart: FusionChart,
        private val defaultSharedPreferences: SharedPreferences
) {
    private lateinit var fusionPersonas: List<PersonaForFusionService>

    private val ownedDLCPersonas by lazy {
        defaultSharedPreferences.getStringSet(PersonaFusionRepository.DLC_SHARED_PREF, emptySet<String>()).orEmpty()
                .map { it.toInt() }
                .mapNotNull { dlcId -> fusionPersonas.firstOrNull { persona -> persona.id == dlcId } }
    }

    fun fusePersona(personaOne: PersonaForFusionService, personaTwo: PersonaForFusionService): PersonaForFusionService? {
        val personasByArcana = fusionPersonas.groupBy { it.arcana }

        return when {
            personaOne == personaTwo || !personasValidInRecipe(personaOne, personaTwo) -> null
            else -> {
                val resultArcana = fusionChart.getResultArcana(personaOne.arcana, personaTwo.arcana)
                resultArcana?.let {
                    val rank = floor(((personaOne.level + personaTwo.level) / 2).toDouble()).toInt() + 1

                    val personasInArcana = checkNotNull(personasByArcana[resultArcana])
                            .filter { it.isValidInResult() }
                            .withIndex()
                            .toList()
                            .sortedBy { it.value.level }

                    if (personaOne.arcana == personaTwo.arcana) {
                        return fuseSameArcanaPersonas(personaOne, personaTwo, rank, personasInArcana)
                    }

                    personasInArcana.firstOrNull { it.value.level >= rank }?.value
                }
            }
        }
    }

    private fun personasValidInRecipe(personaOne: PersonaForFusionService, personaTwo: PersonaForFusionService): Boolean =
            personaOne.isValidInRecipe() && personaTwo.isValidInRecipe()

    private fun PersonaForFusionService.isValidInRecipe() =
            !((isDlc && !isOwnedDLC()) || isRare)

    private fun personasValidInResult(personaOne: PersonaForFusionService, personaTwo: PersonaForFusionService): Boolean =
            personaOne.isValidInResult() && personaTwo.isValidInResult()

    private fun PersonaForFusionService.isValidInResult() =
            !((isDlc && !isOwnedDLC()) || isRare || isSpecial)

    private fun PersonaForFusionService.isOwnedDLC(): Boolean =
            ownedDLCPersonas.contains(this)

    suspend fun initialize() {
        fusionPersonas = fusionRepository.getFusionPersonas()
    }

    private fun fuseSameArcanaPersonas(personaOne: PersonaForFusionService, personaTwo: PersonaForFusionService, rank: Int, personasInArcana: List<IndexedValue<PersonaForFusionService>>): PersonaForFusionService? =
            personasInArcana.lastOrNull {
                it.value.level < rank && !(it.value == personaOne || it.value == personaTwo)
            }?.value
}