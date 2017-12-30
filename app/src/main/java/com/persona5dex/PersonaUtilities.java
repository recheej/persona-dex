package com.persona5dex;

import com.persona5dex.models.Enumerations;
import com.persona5dex.models.Persona;

import java.util.ArrayList;
import java.util.HashMap;

import static com.persona5dex.models.Enumerations.*;

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
            String arcanaStringFormatted = arcana.getName().replaceAll("\\s+", "")
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
