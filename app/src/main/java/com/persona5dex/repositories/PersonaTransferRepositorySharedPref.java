package com.persona5dex.repositories;

import android.content.SharedPreferences;

import com.persona5dex.models.Persona;
import com.google.gson.Gson;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Rechee on 7/23/2017.
 */

public class PersonaTransferRepositorySharedPref implements PersonaTransferRepository {

    private final SharedPreferences transferSharedPreferences;
    private final Gson gson;
    private final SharedPreferences.Editor transferEditor;
    private final SharedPreferences dlcSharedPreferences;
    private final SharedPreferences.Editor dlcEditor;
    private final SharedPreferences defaultSharedPreferences;
    private final String dlcPrefKey;
    private final String rarePersonaInFusionKey;

    public PersonaTransferRepositorySharedPref(SharedPreferences transferSharedPreferences,
                                               SharedPreferences dlcSharedPreferences,
                                               SharedPreferences defaultSharedPreferences,
                                               Gson gson,
                                               String dlcPrefKey,
                                               String rarePersonaInFusionKey){
        this.transferSharedPreferences = transferSharedPreferences;
        this.transferEditor = transferSharedPreferences.edit();

        this.dlcSharedPreferences = dlcSharedPreferences;
        this.dlcEditor = dlcSharedPreferences.edit();

        this.defaultSharedPreferences = defaultSharedPreferences;
        this.dlcPrefKey = dlcPrefKey;

        this.rarePersonaInFusionKey = rarePersonaInFusionKey;

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
        dlcEditor.apply();
    }

    public void setPersonaIDs(Persona[] personas){
        for (int i = 0; i < personas.length; i++) {

            transferEditor.putInt(personas[i].name, i);
            transferEditor.putString(Integer.toString(i), personas[i].name);

            if(personas[i].dlc){
                dlcEditor.putInt(personas[i].name, i);
            }

            personas[i].id = i;
        }
    }

    public Map<String, Integer> getDLCPersonaForSettings() {
        return (Map<String, Integer>) dlcSharedPreferences.getAll();
    }

    public Set<String> getOwnedDlCPersonaIDs() {
        return defaultSharedPreferences.getStringSet(dlcPrefKey, new HashSet<String>(1));
    }

    @Override
    public boolean rarePersonaAllowedInFusions() {
        return defaultSharedPreferences.getBoolean(rarePersonaInFusionKey, true);
    }
}
