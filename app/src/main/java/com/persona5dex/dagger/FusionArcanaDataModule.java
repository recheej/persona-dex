package com.persona5dex.dagger;

import com.persona5dex.models.Persona;
import com.persona5dex.repositories.PersonaRepository;

import java.util.Arrays;
import java.util.Comparator;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Rechee on 6/11/2017.
 */

@Module
public class FusionArcanaDataModule {

    @FusionServiceScope
    @Provides
    @Named("personaByLevel")
    Persona[] personaByLevel(PersonaRepository repository) {
        Persona[] personas = repository.allPersonas();
        Persona[] personsSortedByLevel = new Persona[personas.length];

        System.arraycopy(personas, 0, personsSortedByLevel, 0, personas.length);
        Arrays.sort(personsSortedByLevel, new Comparator<Persona>() {
            @Override
            public int compare(Persona o1, Persona o2) {
                if(o1.level < o2.level){
                    return -1;
                }

                if(o1.level == o2.level){
                    return 0;
                }

                return 1;
            }
        });

        return personsSortedByLevel;
    }
}
