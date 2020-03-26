package com.persona5dex.repositories

import androidx.lifecycle.LiveData
import com.persona5dex.models.MainListPersona
import com.persona5dex.models.room.PersonaDatabase
import javax.inject.Inject

/**
 * Created by Rechee on 11/18/2017.
 */
class MainPersonaRoomRepository @Inject constructor(private val db: PersonaDatabase) : MainPersonaRepository {
    override val allPersonasForMainListLiveData: LiveData<List<MainListPersona>>
        get() = db.personaDao().allPersonasForMainListLiveData

    override val allPersonasForMainList: List<MainListPersona>
        get() = db.personaDao().allPersonasForMainList

    override val dLCPersonas: LiveData<List<MainListPersona>>
        get() = db.personaDao().dLCPersonas

    override fun getPersonaName(personaID: Int): LiveData<String?> {
        return db.personaDao().getPersonaName(personaID)
    }
}