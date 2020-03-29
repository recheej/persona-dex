package com.persona5dex.repositories

import com.persona5dex.dagger.activity.ActivityScope
import com.persona5dex.fragments.PersonaListRepositoryType
import com.persona5dex.models.MainListPersona
import com.persona5dex.models.PersonaRepository
import dagger.Lazy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

@ActivityScope
class PersonaListRepositoryFactory @Inject constructor(
        private val mainListRepository: Lazy<MainListRepository>,
        private val personaSkillsRepository: Lazy<PersonaSkillsRepository>,
        @Named("skillId") private val skillId: Int?,
        private val advancedPersonaListRepository: Lazy<AdvancedPersonaListRepository>
) {
    private val skillRepository by lazy {
        object : PersonaRepository {
            override suspend fun getPersonas(): List<MainListPersona> =
                    skillId?.let {
                        withContext(Dispatchers.IO) {
                            personaSkillsRepository.get().getPersonasWithSkill(it)
                        }
                    }.orEmpty()
        }
    }

    fun getPersonaListRepository(personaListRepositoryType: PersonaListRepositoryType): PersonaRepository =
            when (personaListRepositoryType) {
                PersonaListRepositoryType.PERSONA -> mainListRepository.get()
                PersonaListRepositoryType.SKILLS -> skillRepository
                PersonaListRepositoryType.ADVANCED -> advancedPersonaListRepository.get()
            }
}