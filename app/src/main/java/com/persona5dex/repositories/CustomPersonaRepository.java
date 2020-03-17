package com.persona5dex.repositories;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.persona5dex.models.MainListPersona;

import java.util.ArrayList;
import java.util.Collections;
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
    public LiveData<List<MainListPersona>> getAllPersonasForMainListLiveData() {
        return Transformations.map(personasLiveData, input -> {
             Collections.sort(input, (p1, p2) -> p1.getName().compareTo(p2.getName()));
             return input;
        });
    }

    @Override
    public List<MainListPersona> getAllPersonasForMainList() {
        return personas;
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
                data.setValue(persona.getName());
            }
        }

        return data;
    }
}
