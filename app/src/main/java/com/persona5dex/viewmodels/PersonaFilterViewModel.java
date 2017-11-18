package com.persona5dex.viewmodels;

import com.persona5dex.models.ArcanaMap;
import com.persona5dex.models.Enumerations;

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

        ArcanaMap[] maps = new ArcanaMap[arcanas.length];

        for (int i = 0; i < arcanas.length; i++) {
            Enumerations.Arcana arcana = arcanas[i];
            String name = arcana.name();

            //we are iterating though all arcana enumns, but we are adding to the +1 index since we manulaly added one
            maps[i] = new ArcanaMap();
            maps[i].arcana = arcana;
            maps[i].name = name;
        }

        Arrays.sort(maps, new Comparator<ArcanaMap>() {
            @Override
            public int compare(ArcanaMap o1, ArcanaMap o2) {

                if(o1.arcana == Enumerations.Arcana.ANY && o2.arcana == Enumerations.Arcana.ANY){
                    return 0;
                }

                if(o1.arcana == Enumerations.Arcana.ANY){
                    return -1;
                }

                if(o2.arcana == Enumerations.Arcana.ANY){
                    return 1;
                }

                return o1.name.compareTo(o2.name);
            }
        });

        return maps;
    }
}
