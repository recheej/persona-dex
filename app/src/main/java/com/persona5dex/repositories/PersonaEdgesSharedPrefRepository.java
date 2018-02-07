package com.persona5dex.repositories;

import android.content.SharedPreferences;

import com.persona5dex.BuildConfig;
import com.persona5dex.models.PersonaStore;
import com.persona5dex.models.RawPersonaEdge;
import com.google.gson.Gson;
import com.persona5dex.models.room.PersonaDao;
import com.persona5dex.models.room.PersonaDatabase;
import com.persona5dex.models.room.PersonaFusion;

/**
 * Created by Rechee on 7/22/2017.
 */

public class PersonaEdgesSharedPrefRepository implements PersonaEdgesRepository {
    private final SharedPreferences sharedPreferences;
    private final Gson gson;
    private final SharedPreferences.Editor editor;
    private final PersonaDao personaDao;
    private final PersonaDatabase personaDatabase;

    public PersonaEdgesSharedPrefRepository(SharedPreferences sharedPreferences, Gson gson, PersonaDatabase personaDatabase){
        this.personaDatabase = personaDatabase;
        this.personaDao = personaDatabase.personaDao();
        this.sharedPreferences = sharedPreferences;
        this.gson = gson;
        editor = sharedPreferences.edit();
    }

    @Override
    public void addPersonaEdges(PersonaStore personaStore) {
        for (RawPersonaEdge rawPersonaEdge : personaStore.edgesFrom()) {
            storeEdge(rawPersonaEdge);
        }

        for (RawPersonaEdge rawPersonaEdge : personaStore.edgesTo()) {
            storeEdge(rawPersonaEdge);
        }
    }

    private void storeEdge(RawPersonaEdge rawPersonaEdge) {
        PersonaFusion personaFusion = new PersonaFusion();
        personaFusion.personaOneID = rawPersonaEdge.start;
        personaFusion.personaTwoID = rawPersonaEdge.pairPersona;
        personaFusion.personaResultID = rawPersonaEdge.end;
        personaDao.insertPersonaFusion(personaFusion);
    }

    @Override
    public void markInit() {
        editor.putBoolean("finished", false);
        editor.commit();
        personaDao.removeAllFusions();
        personaDatabase.beginTransaction();
    }

    @Override
    public void markFinished(){
        editor.putBoolean("finished", true);
        editor.commit();
        personaDatabase.setTransactionSuccessful();
        personaDatabase.endTransaction();
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

    public int getEdgesVersionCode() {
        int storedVersionCode = sharedPreferences.getInt(PersonaStore.FUSION_VERSION_KEY, -1);
        if(storedVersionCode == -1){
            storedVersionCode = 1;
            this.updateEdgesVersion(storedVersionCode);
        }

        return storedVersionCode;
    }

    public void updateEdgesVersion(int newVersion) {
        editor.putInt(PersonaStore.FUSION_VERSION_KEY, newVersion);
        editor.commit();
    }
}