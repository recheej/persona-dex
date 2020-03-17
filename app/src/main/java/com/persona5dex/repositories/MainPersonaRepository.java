package com.persona5dex.repositories;

import androidx.lifecycle.LiveData;

import com.persona5dex.models.MainListPersona;

import java.util.List;

/**
 * Created by Rechee on 11/18/2017.
 */

public interface MainPersonaRepository {
    LiveData<List<MainListPersona>> getAllPersonasForMainListLiveData();
    List<MainListPersona> getAllPersonasForMainList();
    LiveData<List<MainListPersona>> getDLCPersonas();
    LiveData<String> getPersonaName(int personaID);
}
