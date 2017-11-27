package com.persona5dex.repositories;

import android.arch.lifecycle.LiveData;
import android.content.Context;

import com.persona5dex.models.MainListPersona;
import com.persona5dex.models.RawPersona;
import com.persona5dex.models.room.PersonaDatabase;

import java.util.List;

import javax.inject.Inject;

import dagger.Lazy;

/**
 * Created by Rechee on 11/18/2017.
 */

public class MainPersonaRoomRepository implements MainPersonaRepository {

    private final PersonaDatabase db;
    private final Lazy<RawPersona[]> rawPersonas;
    private final Context applicationContext;

    @Inject
    public MainPersonaRoomRepository(PersonaDatabase personaDatabase, Lazy<RawPersona[]> rawPersonas, Context applicationContext){
        this.db = personaDatabase;
        this.rawPersonas = rawPersonas;
        this.applicationContext = applicationContext;
    }

    @Override
    public LiveData<List<MainListPersona>> getPersonasForMainList() {
        return db.personaDao().getAllPersonasForMainList();
    }
}
