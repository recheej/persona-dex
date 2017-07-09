package com.example.rechee.persona5calculator;

import com.example.rechee.persona5calculator.models.Enumerations;

import java.util.HashMap;

/**
 * Created by Rechee on 7/8/2017.
 */

public class PersonaUtilities {
    public static HashMap<String, Enumerations.Arcana> arcanaHashMap() {
        HashMap<String, Enumerations.Arcana> arcanaHashMap = new HashMap<>();
        for(Enumerations.Arcana arcana: Enumerations.Arcana.values()){
            String arcanaStringFormatted = arcana.name().replaceAll("\\s+", "")
                    .replaceAll("_", "").toLowerCase();

            arcanaHashMap.put(arcanaStringFormatted, arcana);
        }
        return arcanaHashMap;
    }
}
