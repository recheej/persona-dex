package com.persona5dex.repositories

import com.persona5dex.models.MainListPersona
import com.persona5dex.models.PersonaRepository
import com.persona5dex.models.room.PersonaDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MainListRepository @Inject constructor(private val dao: PersonaDao) : PersonaRepository {
    override suspend fun getPersonas(): List<MainListPersona> =
            withContext(Dispatchers.IO) {
                dao.allPersonasForMainList
            }
}