package com.example.rechee.persona5calculator;

import com.example.rechee.persona5calculator.models.Enumerations;
import com.example.rechee.persona5calculator.models.Persona;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import static com.example.rechee.persona5calculator.models.Enumerations.*;

/**
 * Created by Rechee on 7/8/2017.
 */

public class PersonaUtilities {

    private HashMap<String, Enumerations.Arcana> arcanaHashMap;

    public static final String SHARED_PREF_FUSIONS = "personaFusions";
    public static final String SHARED_PREF_COMMON = "personaFusionsCommon";
    public static final String SHARED_PREF_TRANSFER_CONTENT = "personaTransferContent";

    public PersonaUtilities(){
        this.arcanaHashMap = PersonaUtilities.arcanaHashMap();
    }

    public Arcana nameToArcana(String rawArcanaName){
        String arcanaStringFormatted = rawArcanaName.replaceAll("\\s+", "")
                .replaceAll("_", "").toLowerCase();

        return this.arcanaHashMap.get(arcanaStringFormatted);
    }

    public static PersonaUtilities getUtilities() {
        return new PersonaUtilities();
    }

    public static HashMap<String, Enumerations.Arcana> arcanaHashMap() {
        HashMap<String, Enumerations.Arcana> arcanaHashMap = new HashMap<>();
        for(Arcana arcana: Arcana.values()){
            String arcanaStringFormatted = arcana.name().replaceAll("\\s+", "")
                    .replaceAll("_", "").toLowerCase();

            arcanaHashMap.put(arcanaStringFormatted, arcana);
        }
        return arcanaHashMap;
    }

    public static Persona[] filterPersonaByName(Persona[] personasToFilter, String nameQuery){
        ArrayList<Persona> filteredSuggestions = new ArrayList<>(personasToFilter.length);

        String nameQueryLowerCase = nameQuery.toLowerCase();
        for (Persona suggestion: personasToFilter){
            String personaName = suggestion.name.toLowerCase();

            if(personaName.contains(nameQueryLowerCase)){
                filteredSuggestions.add(suggestion);
            }
        }

        if(filteredSuggestions.size() == 0){
            return new Persona[0];
        }

        return filteredSuggestions.toArray(new Persona[filteredSuggestions.size()]);
    }

    public static String getFileContents(InputStream stream){
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        StringBuilder out = new StringBuilder();
        String line;

        String fileContents = "";
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
}
