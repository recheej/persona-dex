package com.example.rechee.persona5calculator.repositories;

import android.content.SharedPreferences;

import com.example.rechee.persona5calculator.models.Persona;
import com.google.gson.Gson;

/**
 * Created by Rechee on 7/23/2017.
 */

public class PersonaTransferRepositorySharedPref implements PersonaTransferRepository {

    private final SharedPreferences sharedPreferences;
    private final Gson gson;
    private final SharedPreferences fusionSharedPreferences;

    public PersonaTransferRepositorySharedPref(SharedPreferences sharedPreferences, SharedPreferences fusionSharedPrefernces, Gson gson){
        this.sharedPreferences = sharedPreferences;
        this.fusionSharedPreferences = fusionSharedPrefernces;
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

    @Override
    public void storePersonaForFusion(Persona personaForFusion) {
        int personaID = fusionSharedPreferences.getInt(personaForFusion.name, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("personaForFusion", personaID);
        editor.commit();
    }

    @Override
    public int getPersonaForFusion() {
        return sharedPreferences.getInt("personaForFusion", 0);
    }
}
