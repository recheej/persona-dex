package com.example.rechee.persona5calculator.repositories;

import android.content.SharedPreferences;

import com.example.rechee.persona5calculator.models.Persona;
import com.google.gson.Gson;

import javax.inject.Named;

/**
 * Created by Rechee on 7/23/2017.
 */

public class PersonaTransferRepositorySharedPref implements PersonaTransferRepository {

    private final SharedPreferences sharedPreferences;
    private final Gson gson;

    public PersonaTransferRepositorySharedPref(SharedPreferences sharedPreferences, Gson gson){
        this.sharedPreferences = sharedPreferences;
        this.gson = gson;
    }

    @Override
    public void storePersonaForDetail(Persona persona) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("detailPersona", gson.toJson(persona, Persona.class));
        editor.commit();
    }

    @Override
    public Persona getDetailPersona() {
        String personaDetailJson = sharedPreferences.getString("detailPersona", "");
        return gson.fromJson(personaDetailJson, Persona.class);
    }
}
