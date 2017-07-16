package com.example.rechee.persona5calculator;

import com.example.rechee.persona5calculator.models.Enumerations;
import com.example.rechee.persona5calculator.models.Persona;

import java.util.ArrayList;
import java.util.HashMap;

import static com.example.rechee.persona5calculator.models.Enumerations.*;

/**
 * Created by Rechee on 7/8/2017.
 */

public class PersonaUtilities {

    private HashMap<String, Enumerations.Arcana> arcanaHashMap;

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
        for (Persona suggestion: personasToFilter){
            String personaName = suggestion.name.toLowerCase();

            if(personaName.contains(nameQuery)){
                filteredSuggestions.add(suggestion);
            }
        }

        if(filteredSuggestions.size() == 0){
            return new Persona[0];
        }

        return filteredSuggestions.toArray(new Persona[filteredSuggestions.size()]);
    }
}
