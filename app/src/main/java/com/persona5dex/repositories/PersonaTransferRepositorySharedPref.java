package com.persona5dex.repositories;

import android.content.SharedPreferences;

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
    public String getPersonaName(int personaID) {
        return transferSharedPreferences.getString(Integer.toString(personaID), "");
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
