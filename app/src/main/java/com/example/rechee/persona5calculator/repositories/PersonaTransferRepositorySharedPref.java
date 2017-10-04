package com.example.rechee.persona5calculator.repositories;

import android.content.SharedPreferences;

import com.example.rechee.persona5calculator.models.Persona;
import com.google.gson.Gson;

/**
 * Created by Rechee on 7/23/2017.
 */

public class PersonaTransferRepositorySharedPref implements PersonaTransferRepository {

    private final SharedPreferences transferSharedPreferences;
    private final Gson gson;
    private final SharedPreferences.Editor transferEditor;

    public PersonaTransferRepositorySharedPref(SharedPreferences transferSharedPreferences, Gson gson){
        this.transferSharedPreferences = transferSharedPreferences;
        this.transferEditor = transferSharedPreferences.edit();
        this.gson = gson;
    }

    @Override
    public void storePersonaForDetail(Persona persona) {
        SharedPreferences.Editor editor = transferSharedPreferences.edit();
        editor.putString("detailPersona", gson.toJson(persona, Persona.class));
        editor.commit();
    }

    @Override
    public Persona getDetailPersona() {
        String personaDetailJson = transferSharedPreferences.getString("detailPersona", "");
        return gson.fromJson(personaDetailJson, Persona.class);
    }

    @Override
    public void storePersonaForFusion(Persona personaForFusion) {
        int personaID = transferSharedPreferences.getInt(personaForFusion.name, 0);
        SharedPreferences.Editor editor = transferSharedPreferences.edit();
        editor.putInt("personaForFusion", personaID);
        editor.commit();
    }

    @Override
    public int getPersonaForFusion() {
        return transferSharedPreferences.getInt("personaForFusion", 0);
    }

    @Override
    public String getPersonaName(int personaID) {
        return transferSharedPreferences.getString(Integer.toString(personaID), "");
    }

    @Override
    public void commit() {
        transferEditor.apply();
    }

    public void setPersonaIDs(Persona[] personas){
        for (int i = 0; i < personas.length; i++) {

            transferEditor.putInt(personas[i].name, i);
            transferEditor.putString(Integer.toString(i), personas[i].name);

            personas[i].id = i;
        }
    }
}
