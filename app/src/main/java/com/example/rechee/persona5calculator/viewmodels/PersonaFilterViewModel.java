package com.example.rechee.persona5calculator.viewmodels;

import com.example.rechee.persona5calculator.models.ArcanaMap;
import com.example.rechee.persona5calculator.models.Enumerations;

import java.util.Arrays;
import java.util.Comparator;

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

        int mapsSize = arcanas.length + 1;
        ArcanaMap[] maps = new ArcanaMap[mapsSize];

        maps[0] = new ArcanaMap();
        maps[0].arcana = null;
        maps[0].name = "Any";

        for (int i = 0; i < arcanas.length; i++) {
            Enumerations.Arcana arcana = arcanas[i];
            String name = arcana.name();

            //we are iterating though all arcana enumns, but we are adding to the +1 index since we manulaly added one
            maps[i + 1] = new ArcanaMap();
            maps[i + 1].arcana = arcana;
            maps[i + 1].name = name;
        }

        Arrays.sort(maps, new Comparator<ArcanaMap>() {
            @Override
            public int compare(ArcanaMap o1, ArcanaMap o2) {

                if(o1.arcana == null && o2.arcana == null){
                    return 0;
                }

                if(o1.arcana == null){
                    return -1;
                }

                if(o2.arcana == null){
                    return 1;
                }

                return o1.name.compareTo(o2.name);
            }
        });

        return maps;
    }
}
