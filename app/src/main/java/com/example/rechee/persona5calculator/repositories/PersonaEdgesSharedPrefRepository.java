package com.example.rechee.persona5calculator.repositories;

import android.content.SharedPreferences;

import com.example.rechee.persona5calculator.models.Persona;
import com.example.rechee.persona5calculator.models.PersonaEdgeDisplay;
import com.example.rechee.persona5calculator.models.PersonaStore;
import com.example.rechee.persona5calculator.models.PersonaStoreDisplay;
import com.example.rechee.persona5calculator.models.RawPersonaEdge;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Rechee on 7/22/2017.
 */

public class PersonaEdgesSharedPrefRepository implements PersonaEdgesRepository {
    private final SharedPreferences sharedPreferences;
    private final Gson gson;
    private final SharedPreferences.Editor editor;
    private int personaIDCounter;

    public PersonaEdgesSharedPrefRepository(SharedPreferences sharedPreferences, Gson gson){
        this.sharedPreferences = sharedPreferences;
        this.gson = gson;
        editor = sharedPreferences.edit();
        personaIDCounter = 0;
    }

    @Override
    public void addPersonaEdges(Persona persona, PersonaStore personaStore) {
        editor.putString(Integer.toString(persona.id), gson.toJson(personaStore));
    }

    public void setPersonaIDs(Persona[] personas){

        for (int i = 0; i < personas.length; i++) {
            editor.putInt(personas[i].name, i);
            personas[i].id = i;
        }

        editor.apply();
    }

    @Override
    public void markInit() {
        if(!sharedPreferences.contains("initialized")){
            editor.putBoolean("initialized", true);
            editor.commit();
        }
    }

    @Override
    public void markFinished(){
        editor.putBoolean("finished", true);
        editor.commit();
    }

    @Override
    public PersonaStoreDisplay getEdgesForPersona(int personaID) {
        PersonaStore rawStore = gson.fromJson(sharedPreferences.getString(Integer.toString(personaID), ""), PersonaStore.class);

        //TODO: right now fusions are stored in fusion shared pref and so are the mapping between persona id and persona name
        //we also need a mapping between persona name and id
    }

    private PersonaStoreDisplay mapRawStoreToDisplay(PersonaStore store, int personaID){
        PersonaStoreDisplay personaStoreDisplay = new PersonaStoreDisplay();

        List<PersonaEdgeDisplay> edgesFrom = new ArrayList<>();
        List<PersonaEdgeDisplay> edgesTo = new ArrayList<>();

        for (RawPersonaEdge rawPersonaEdge : store.edgesFrom()) {
            PersonaEdgeDisplay edgeDisplay = new PersonaEdgeDisplay();

            String left;
            if(rawPersonaEdge.start == personaID){
                left = sharedPreferences.get
            }
        }

    }
}