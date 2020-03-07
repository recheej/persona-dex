package com.persona5dex.viewmodels;

import com.persona5dex.ArcanaNameProvider;
import com.persona5dex.ArcanaNameProvider.ArcanaName;
import com.persona5dex.models.Enumerations;

import java.util.Arrays;
import java.util.Comparator;

import javax.inject.Inject;

/**
 * Created by Rechee on 8/14/2017.
 */

public class PersonaFilterViewModel {

    private final ArcanaNameProvider arcanaNameProvider;

    @Inject
    public PersonaFilterViewModel(ArcanaNameProvider arcanaNameProvider){
        this.arcanaNameProvider = arcanaNameProvider;
    }

    public ArcanaName[] getArcanaNames() {
        final ArcanaName[] allArcanaNames = arcanaNameProvider.getAllArcanaNames();
        Arrays.sort(allArcanaNames, new Comparator<ArcanaName>() {
            @Override
            public int compare(ArcanaName o1, ArcanaName o2) {

                if(o1.getArcana() == Enumerations.Arcana.ANY && o2.getArcana() == Enumerations.Arcana.ANY){
                    return 0;
                }

                if(o1.getArcana() == Enumerations.Arcana.ANY){
                    return -1;
                }

                if(o2.getArcana() == Enumerations.Arcana.ANY){
                    return 1;
                }

                return o1.getArcanaName().compareTo(o2.getArcanaName());
            }
        });

        return allArcanaNames;
    }
}
