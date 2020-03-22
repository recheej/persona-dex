package com.persona5dex.fusionService

import com.persona5dex.models.PersonaForFusionService
import com.persona5dex.repositories.PersonaFusionRepository
import javax.inject.Inject

class PersonaFusionGraphGenerator @Inject constructor(
        private val fusionRepository: PersonaFusionRepository,
        private val fusionChartService: FusionChartService
) {
    suspend fun getAllFusions(): List<PersonaGraphEntry> {
        val personaFuser = PersonaFuserV2(fusionRepository, fusionChartService.getFusionChart())
        val personas = fusionRepository.getFusionPersonas()

        val personaFusions = mutableSetOf<PersonaGraphEntry>()
        personas.forEach { personaOne ->
            personas.forEach { personaTwo ->
                personaFuser.fusePersona(personaOne, personaTwo)?.let {
                    val graphEntry = PersonaGraphEntry(personaOne, personaTwo, it)
                    if(graphEntry !in personaFusions) {
                        personaFusions.add(graphEntry)
                    }
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
)