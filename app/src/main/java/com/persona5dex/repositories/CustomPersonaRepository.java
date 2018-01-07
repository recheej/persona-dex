package com.persona5dex.repositories;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;

import com.persona5dex.models.MainListPersona;
import com.persona5dex.models.room.Persona;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by reche on 1/6/2018.
 */

public class CustomPersonaRepository implements MainPersonaRepository {

    private final List<MainListPersona> personas;
    private MutableLiveData<List<MainListPersona>> personasLiveData;
    public CustomPersonaRepository(List<MainListPersona> personas) {
        this.personas = personas;
        personasLiveData = new MutableLiveData<>();
        personasLiveData.setValue(this.personas);
    }

    @Override
    public LiveData<List<MainListPersona>> getAllPersonasForMainList() {
        return personasLiveData;
    }

    @Override
    public LiveData<List<MainListPersona>> getDLCPersonas() {
        return Transformations.map(personasLiveData, personas -> {
            List<MainListPersona> finalList = new ArrayList<>(personas.size());

            for (MainListPersona persona : personas) {
                if(persona.dlc){
                    finalList.add(persona);
                }
            }

            return finalList;
        });
    }

    @Override
    public LiveData<String> getPersonaName(int personaID) {

        MutableLiveData<String> data = new MutableLiveData<>();
        for (MainListPersona persona : personas) {
            if(persona.id == personaID){
                data.setValue(persona.name);
            }
        }

        return data;
    }
}
