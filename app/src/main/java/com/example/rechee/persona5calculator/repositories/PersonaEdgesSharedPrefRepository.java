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
    private final SharedPreferences commonPreferences;
    private final Gson gson;
    private final SharedPreferences.Editor editor;
    private SharedPreferences.Editor commonPreferencesEditor;

    public PersonaEdgesSharedPrefRepository(SharedPreferences sharedPreferences, SharedPreferences commonPreferences, Gson gson){
        this.sharedPreferences = sharedPreferences;
        this.commonPreferences = commonPreferences;
        this.gson = gson;
        editor = sharedPreferences.edit();
        commonPreferencesEditor = commonPreferences.edit();
    }

    @Override
    public void addPersonaEdges(Persona persona, PersonaStore personaStore) {
        editor.putString(persona.name, gson.toJson(personaStore));
    }

    @Override
    public void markInit() {
        if(!commonPreferences.contains("initialized")){
            commonPreferencesEditor.putBoolean("initialized", true);
        }
    }

    @Override
    public void markFinished(){
        editor.putBoolean("finished", true);
        editor.commit();
        commonPreferencesEditor.commit();
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