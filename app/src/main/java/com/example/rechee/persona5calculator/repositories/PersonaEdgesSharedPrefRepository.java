package com.example.rechee.persona5calculator.repositories;

import android.content.SharedPreferences;

import com.example.rechee.persona5calculator.models.Persona;
import com.example.rechee.persona5calculator.models.PersonaStore;
import com.google.gson.Gson;

/**
 * Created by Rechee on 7/22/2017.
 */

public class PersonaEdgesSharedPrefRepository implements PersonaEdgesRepository {
    private final SharedPreferences sharedPreferences;
    private final Gson gson;

    public PersonaEdgesSharedPrefRepository(SharedPreferences sharedPreferences, Gson gson){
        this.sharedPreferences = sharedPreferences;
        this.gson = gson;
    }

    @Override
    public void addPersonaEdges(Persona persona, PersonaStore personaStore) {

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(persona.name, gson.toJson(personaStore));

        if(!sharedPreferences.contains("initialized")){
            editor.putBoolean("initialized", true);
        }

        editor.apply();
    }

    @Override
    public void markFinished(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("finished", true);
        editor.apply();
    }

    @Override
    public PersonaStore getEdgesForPersona(Persona persona) {
        return gson.fromJson(sharedPreferences.getString(persona.name, ""), PersonaStore.class);
    }
}