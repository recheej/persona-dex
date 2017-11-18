package com.persona5dex.repositories;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.os.AsyncTask;
import android.support.annotation.Nullable;

import com.persona5dex.models.RawPersona;
import com.persona5dex.models.MainListPersona;
import com.persona5dex.models.room.PersonaDatabase;

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

    @Inject
    public MainPersonaRoomRepository(PersonaDatabase personaDatabase, Lazy<RawPersona[]> rawPersonas){
        this.db = personaDatabase;
        this.rawPersonas = rawPersonas;
    }

    @Override
    public LiveData<List<MainListPersona>> getPersonasForMainList() {

        final MutableLiveData<List<MainListPersona>> data = new MutableLiveData<>();

        final LiveData<List<MainListPersona>> allPersonasForMainList = db.personaDao().getAllPersonasForMainList();

        allPersonasForMainList.observeForever(new Observer<List<MainListPersona>>() {
            @Override
            public void onChanged(@Nullable List<MainListPersona> mainListPersonas) {
                if(mainListPersonas == null || mainListPersonas.size() == 0){
                    //for now, let's fall back to getting the personas from the file
                    new GetPersonasFromFileTask().execute(rawPersonas.get(), data);
                }
                else{
                    data.setValue(allPersonasForMainList.getValue());
                }
            }
        });

        return data;
    }

    static public class GetPersonasFromFileTask extends AsyncTask<Object, Void, Void> {

        @Override
        protected Void doInBackground(Object... objects) {
            RawPersona[] rawPersonas = (RawPersona[]) objects[0];
            MutableLiveData<List<MainListPersona>> data = (MutableLiveData<List<MainListPersona>>) objects[1];

            List<MainListPersona> personaList = new ArrayList<>(rawPersonas.length);
            for (RawPersona rawPersona : rawPersonas) {
                MainListPersona persona = new MainListPersona();
                persona.arcana = rawPersona.arcana;
                persona.name = rawPersona.name;
                persona.level = rawPersona.level;

                personaList.add(persona);
            }
            data.postValue(personaList);

            return null;
        }
    }
}
