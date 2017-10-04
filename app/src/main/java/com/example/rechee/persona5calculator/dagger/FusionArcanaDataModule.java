package com.example.rechee.persona5calculator.dagger;

import android.util.SparseArray;

import com.example.rechee.persona5calculator.PersonaFileUtilities;
import com.example.rechee.persona5calculator.PersonaUtilities;
import com.example.rechee.persona5calculator.models.Enumerations.Arcana;
import com.example.rechee.persona5calculator.models.Persona;
import com.example.rechee.persona5calculator.repositories.PersonaRepository;
import com.example.rechee.persona5calculator.repositories.PersonaRepositoryFile;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

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
