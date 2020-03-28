package com.persona5dex.fusionService

import com.persona5dex.models.PersonaForFusionService
import com.persona5dex.repositories.PersonaFusionRepository
import kotlinx.coroutines.yield
import javax.inject.Inject

class PersonaFusionGraphGenerator @Inject constructor(
        private val fusionRepository: PersonaFusionRepository,
        private val fusionChartService: FusionChartService
) {
    suspend fun getAllFusions(): List<PersonaGraphEntry> {
        val personas = fusionRepository.getFusionPersonas()
        yield()
        val personaFuser = PersonaFuserV2(personas, fusionChartService.getFusionChart())
        yield()

        val personaFusions = mutableSetOf<PersonaGraphEntry>()
        personas.forEach { personaOne ->
            personas.forEach { personaTwo ->
                personaFuser.fusePersona(personaOne, personaTwo)?.let {
                    val graphEntry = PersonaGraphEntry(personaOne, personaTwo, it)
                    personaFusions.add(graphEntry)
                }
            }
        }

        return personaFusions.toList()
    }
}

data class PersonaGraphEntry(
        val personaOne: PersonaForFusionService,
        val personaTwo: PersonaForFusionService,
        val resultPersona: PersonaForFusionService
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PersonaGraphEntry

        if(personaOne == other.personaTwo && other.personaOne == personaTwo) {
            return resultPersona == other.resultPersona
        }

        if (personaOne != other.personaOne) return false
        if (personaTwo != other.personaTwo) return false
        if (resultPersona != other.resultPersona) return false

        return true
    }

    override fun hashCode(): Int {
        val firstPersona = if(personaOne.id < personaTwo.id) personaOne else personaTwo
        val secondPersona = if(firstPersona.id == personaTwo.id) personaOne else personaTwo

        var result = firstPersona.hashCode()
        result = 31 * result + secondPersona.hashCode()
        result = 31 * result + resultPersona.hashCode()
        return result
    }
}