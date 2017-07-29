package com.example.rechee.persona5calculator.dagger;

import android.util.SparseArray;

import com.example.rechee.persona5calculator.PersonaUtilities;
import com.example.rechee.persona5calculator.models.Enumerations.Arcana;
import com.example.rechee.persona5calculator.models.Persona;
import com.example.rechee.persona5calculator.models.RawArcanaMap;
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
    HashMap<Arcana, HashMap<Arcana, Arcana>> arcanaTable(RawArcanaMap[] arcanaMaps) {
        PersonaUtilities personaUtilities = PersonaUtilities.getUtilities();

        HashMap<Arcana, HashMap<Arcana, Arcana>> arcanaTable = new HashMap<>(30);
        for (RawArcanaMap arcanaMap: arcanaMaps){
            Arcana arcanaPersonaOne = personaUtilities.nameToArcana(arcanaMap.source[0]);
            Arcana arcanaPersonaTwo = personaUtilities.nameToArcana(arcanaMap.source[1]);

            Arcana resultArcana = personaUtilities.nameToArcana(arcanaMap.result);

            if(arcanaTable.containsKey(arcanaPersonaOne)){
                arcanaTable.get(arcanaPersonaOne).put(arcanaPersonaTwo, resultArcana);
            }
            else{
                HashMap<Arcana, Arcana> innerTable = new HashMap<>();
                innerTable.put(arcanaPersonaTwo, resultArcana);
                arcanaTable.put(arcanaPersonaOne, innerTable);
            }
        }

        return arcanaTable;
    }

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

    @FusionServiceScope
    @Provides
    SparseArray<List<Persona>> personaByArcana(@Named("personaByLevel") Persona[] personas, HashMap<Arcana, HashMap<Arcana, Arcana>> arcanaTable){
        SparseArray<List<Persona>> personaByArcana = new SparseArray<>(arcanaTable.size());
        for(Persona persona: personas){
            int arcanaIndex = persona.getArcana().ordinal();
            List<Persona> personaList = personaByArcana.get(arcanaIndex);

            if(personaList == null){
                personaList = new ArrayList<>();
                personaList.add(persona);
                personaByArcana.put(persona.getArcana().ordinal(), personaList);
            }
            else{
                personaList.add(persona);
            }
        }

        return personaByArcana;
    }

    @Provides
    @FusionServiceScope
    PersonaRepository provideRepository(@Named("personaFileContents") String personaFileContents, Gson gson) {
        return new PersonaRepositoryFile(personaFileContents, gson);
    }

    @Provides
    @FusionServiceScope
    Gson gson() {
        return new Gson();
    }
}
