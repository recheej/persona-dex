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
    private final SharedPreferences.Editor editor;

    public PersonaEdgesSharedPrefRepository(SharedPreferences sharedPreferences, Gson gson){
        this.sharedPreferences = sharedPreferences;
        this.gson = gson;
        editor = sharedPreferences.edit();
    }

    @Override
    public void addPersonaEdges(Persona persona, PersonaStore personaStore) {
        editor.putString(persona.name, gson.toJson(personaStore));
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
    public PersonaStore getEdgesForPersona(Persona persona) {
        return gson.fromJson(sharedPreferences.getString(persona.name, ""), PersonaStore.class);
    }

    @Override
    public PersonaStore getEdgesForPersona(String personaName) {
        return gson.fromJson(sharedPreferences.getString(personaName, ""), PersonaStore.class);
    }
}