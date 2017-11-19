package com.persona5dex.repositories;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.Transformations;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.Nullable;

import com.persona5dex.models.RawPersona;
import com.persona5dex.models.MainListPersona;
import com.persona5dex.models.room.PersonaDatabase;
import com.persona5dex.services.CreateDatabaseJobService;

import java.util.ArrayList;
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
        return Transformations.switchMap(db.personaDao().getAllPersonasForMainList(), new Function<List<MainListPersona>, LiveData<List<MainListPersona>>>() {
            @Override
            public LiveData<List<MainListPersona>> apply(List<MainListPersona> input) {
                final MutableLiveData<List<MainListPersona>> data = new MutableLiveData<>();
                if(input == null || input.size() == 0){
                    CreateDatabaseJobService.enqueueWork(applicationContext,
                            new Intent(applicationContext, CreateDatabaseJobService.class));

                    //for now, let's fall back to getting the personas from the file
                    new GetPersonasFromFileTask().execute(rawPersonas.get(), data);
                }
                else{
                    data.setValue(input);
                }

                return data;
            }
        });
    }

    static public class GetPersonasFromFileTask extends AsyncTask<Object, Void, Void> {

        @Override
        protected Void doInBackground(Object... objects) {
            RawPersona[] rawPersonas = (RawPersona[]) objects[0];
            MutableLiveData<List<MainListPersona>> data = (MutableLiveData<List<MainListPersona>>) objects[1];

            List<MainListPersona> personaList = new ArrayList<>(rawPersonas.length);
            for (RawPersona rawPersona : rawPersonas) {
                MainListPersona persona = new MainListPersona();
                persona.arcanaName = rawPersona.arcana;
                persona.name = rawPersona.name;
                persona.level = rawPersona.level;

                personaList.add(persona);
            }
            data.postValue(personaList);

            return null;
        }
    }
}
