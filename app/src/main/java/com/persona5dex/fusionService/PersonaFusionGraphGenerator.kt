package com.persona5dex.fusionService

import com.persona5dex.models.PersonaForFusionService
import com.persona5dex.repositories.PersonaFusionRepository
import javax.inject.Inject

class PersonaFusionGraphGenerator @Inject constructor(
        private val fusionRepository: PersonaFusionRepository,
        private val personaFuser: PersonaFuserV2
) {
    suspend fun getAllFusions(): List<PersonaGraphEntry> {
        val personas = fusionRepository.getFusionPersonas()

        val personaFusions = mutableListOf<PersonaGraphEntry>()
        personas.forEach { personaOne ->
            personas.forEach { personaTwo ->
                personaFuser.fusePersona(personaOne, personaTwo)?.let {
                    personaFusions.add(PersonaGraphEntry(personaOne, personaTwo, it))
                }
            }
        }

        return personaFusions
    }
}

data class PersonaGraphEntry(
        val personaOne: PersonaForFusionService,
        val personaTwo: PersonaForFusionService,
        val resultPersona: PersonaForFusionService
)