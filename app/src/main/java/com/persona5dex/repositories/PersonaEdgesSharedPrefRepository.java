package com.persona5dex.repositories;

import android.content.SharedPreferences;

import com.persona5dex.models.Persona;
import com.persona5dex.models.PersonaEdgeDisplay;
import com.persona5dex.models.PersonaStore;
import com.persona5dex.models.PersonaStoreDisplay;
import com.persona5dex.models.RawPersonaEdge;
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

    public PersonaEdgesSharedPrefRepository(SharedPreferences sharedPreferences, Gson gson){
        this.sharedPreferences = sharedPreferences;
        this.gson = gson;
        editor = sharedPreferences.edit();
    }

    @Override
    public void addPersonaEdges(Persona persona, PersonaStore personaStore) {
        editor.putString(Integer.toString(persona.id), gson.toJson(personaStore));
    }

    @Override
    public void markInit() {
        editor.putBoolean("finished", false);
        editor.commit();
    }

    @Override
    public void markFinished(){
        editor.putBoolean("finished", true);
        editor.commit();
    }

    @Override
    public PersonaStore getEdgesForPersona(int personaID) {
        final String jsonString = sharedPreferences.getString(Integer.toString(personaID), "");
        return gson.fromJson(jsonString, PersonaStore.class);
    }

    @Override
    public boolean edgesStored() {
        return sharedPreferences.getBoolean("finished", false);
    }
}