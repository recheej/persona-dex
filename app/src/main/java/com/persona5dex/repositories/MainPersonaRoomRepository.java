package com.persona5dex.repositories;

import android.arch.lifecycle.LiveData;
import android.content.Context;

import com.persona5dex.models.MainListPersona;
import com.persona5dex.models.RawPersona;
import com.persona5dex.models.room.Persona;
import com.persona5dex.models.room.PersonaDatabase;

import java.util.List;

import javax.inject.Inject;

import dagger.Lazy;

/**
 * Created by Rechee on 11/18/2017.
 */

public class MainPersonaRoomRepository implements MainPersonaRepository {

    private final PersonaDatabase db;

    @Inject
    public MainPersonaRoomRepository(PersonaDatabase personaDatabase, Lazy<RawPersona[]> rawPersonas, Context applicationContext){
        this.db = personaDatabase;
    }

    @Override
    public LiveData<List<MainListPersona>> getPersonasForMainList() {
        return db.personaDao().getAllPersonasForMainList();
    }

    @Override
    public LiveData<List<Persona>> getDLCPersonas() {
        return db.personaDao().getDLCPersonas();
    }
}
