package com.persona5dex;

import androidx.annotation.NonNull;

import com.persona5dex.dagger.application.ApplicationScope;
import com.persona5dex.models.RawArcanaMap;
import com.persona5dex.models.RawPersona;
import com.persona5dex.models.RawRarePersonaMap;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import static com.persona5dex.models.Enumerations.*;

/**
 * Created by Rechee on 9/24/2017.
 */

@ApplicationScope
public class PersonaFileUtilities {

    private final Gson gson;
    private final ArcanaNameProvider arcanaNameProvider;

    @Inject
    public PersonaFileUtilities(Gson gson, ArcanaNameProvider arcanaNameProvider) {
        this.gson = gson;
        this.arcanaNameProvider = arcanaNameProvider;
    }

    @NonNull public String getFileContents(InputStream stream){
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        StringBuilder out = new StringBuilder();
        String line;

        String fileContents;
        try {
            while ((line = reader.readLine()) != null) {
                out.append(line);
            }
            reader.close();

            fileContents = out.toString();

        } catch (IOException e) {
            fileContents = "";
        }

        return fileContents;
    }

    public HashMap<Arcana, HashMap<Arcana, Arcana>> getArcanaTable(InputStream arcanaComboFile){
        String arcanaMapFileContents = this.getFileContents(arcanaComboFile);

        RawArcanaMap[] arcanaMaps = gson.fromJson(arcanaMapFileContents, RawArcanaMap[].class);
        HashMap<Arcana, HashMap<Arcana, Arcana>> arcanaTable = new HashMap<>(30);
        for (RawArcanaMap arcanaMap: arcanaMaps){
            Arcana arcanaPersonaOne = arcanaNameProvider.getArcanaForEnglishName(arcanaMap.source[0]);
            Arcana arcanaPersonaTwo = arcanaNameProvider.getArcanaForEnglishName(arcanaMap.source[1]);

            Arcana resultArcana = arcanaNameProvider.getArcanaForEnglishName(arcanaMap.result);

            if(arcanaTable.containsKey(arcanaPersonaOne)){
                arcanaTable.get(arcanaPersonaOne).put(arcanaPersonaTwo, resultArcana);
            }
            else{
                HashMap<Arcana, Arcana> innerTable = new HashMap<>();
                innerTable.put(arcanaPersonaTwo, resultArcana);
                arcanaTable.put(arcanaPersonaOne, innerTable);
            }
        }

        return arcanaTable;
    }

    public <T> T parseJsonFile(InputStream stream, Class<T> tClass){
        String contents = this.getFileContents(stream);
        return gson.fromJson(contents, tClass);
    }

    public Map<String, int[]> rareCombos(InputStream rareComboFile) {
        String personaFileContents = this.getFileContents(rareComboFile);
        RawRarePersonaMap[] rawRarePersonaMaps = gson.fromJson(personaFileContents, RawRarePersonaMap[].class);

        Map<String, int[]> rareComboMaps = new HashMap<>(21);

        for (RawRarePersonaMap rawRarePersonaMap : rawRarePersonaMaps) {
            rareComboMaps.put(rawRarePersonaMap.name, rawRarePersonaMap.modifiers);
        }

        return rareComboMaps;
    }
}
