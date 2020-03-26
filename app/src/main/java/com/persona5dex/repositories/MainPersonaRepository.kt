package com.persona5dex.repositories

import androidx.lifecycle.LiveData
import com.persona5dex.models.MainListPersona

/**
 * Created by Rechee on 11/18/2017.
 */
interface MainPersonaRepository {
    val allPersonasForMainListLiveData: LiveData<List<MainListPersona>>
    val allPersonasForMainList: List<MainListPersona>
    val dLCPersonas: LiveData<List<MainListPersona>>
    fun getPersonaName(personaID: Int): LiveData<String?>
}