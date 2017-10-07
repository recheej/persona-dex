package com.example.rechee.persona5calculator;

import android.util.SparseArray;

import com.example.rechee.persona5calculator.models.Enumerations;
import com.example.rechee.persona5calculator.models.Persona;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.example.rechee.persona5calculator.models.Enumerations.*;

/**
 * Created by Rechee on 7/8/2017.
 */

public class PersonaUtilities {

    private HashMap<String, Enumerations.Arcana> arcanaHashMap;

    public static final String SHARED_PREF_FUSIONS = "personaFusions";
    public static final String SHARED_PREF_TRANSFER_CONTENT = "personaTransferContent";
    public static final String SHARED_PREF_DLC = "personaDLCContent";

    public PersonaUtilities(){
        this.arcanaHashMap = PersonaUtilities.arcanaHashMap();
    }

    Arcana nameToArcana(String rawArcanaName){
        String arcanaStringFormatted = rawArcanaName.replaceAll("\\s+", "")
                .replaceAll("_", "").toLowerCase();

        return this.arcanaHashMap.get(arcanaStringFormatted);
    }

    static PersonaUtilities getUtilities() {
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
}
