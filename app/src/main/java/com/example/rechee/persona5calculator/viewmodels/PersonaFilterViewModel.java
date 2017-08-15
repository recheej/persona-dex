package com.example.rechee.persona5calculator.viewmodels;

import com.example.rechee.persona5calculator.models.ArcanaMap;
import com.example.rechee.persona5calculator.models.Enumerations;

import javax.inject.Inject;

/**
 * Created by Rechee on 8/14/2017.
 */

public class PersonaFilterViewModel {

    @Inject
    public PersonaFilterViewModel(){

    }

    public ArcanaMap[] getArcanaMaps() {
        Enumerations.Arcana[] arcanas = Enumerations.Arcana.values();
        ArcanaMap[] maps = new ArcanaMap[arcanas.length];
        for (int i = 0; i < arcanas.length; i++) {
            Enumerations.Arcana arcana = arcanas[i];
            String name = arcana.name();

            maps[i] = new ArcanaMap();
            maps[i].arcana = arcana;
            maps[i].name = name;
        }

        return maps;
    }
}
