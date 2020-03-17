package com.persona5dex.repositories;

import androidx.lifecycle.LiveData;

import com.persona5dex.models.MainListPersona;
import com.persona5dex.models.room.PersonaDatabase;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Rechee on 11/18/2017.
 */

public class MainPersonaRoomRepository implements MainPersonaRepository {

    private final PersonaDatabase db;

    @Inject
    public MainPersonaRoomRepository(PersonaDatabase personaDatabase){
        this.db = personaDatabase;
    }

    @Override
    public LiveData<List<MainListPersona>> getAllPersonasForMainList() {
        return db.personaDao().getAllPersonasForMainListLiveData();
    }

    @Override
    public LiveData<List<MainListPersona>> getDLCPersonas() {
        return db.personaDao().getDLCPersonas();
    }

    @Override
    public LiveData<String> getPersonaName(int personaID) {
        return db.personaDao().getPersonaName(personaID);
    }
}
