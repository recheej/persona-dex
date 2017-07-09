package com.example.rechee.persona5calculator.repositories;

import com.example.rechee.persona5calculator.models.Persona;
import com.example.rechee.persona5calculator.models.RawPersona;
import com.google.gson.Gson;
import java.util.Arrays;
import java.util.Comparator;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by Rechee on 7/1/2017.
 */

public class PersonaRepositoryFile implements PersonaRepository {

    private final String personaFileContents;
    private final Gson gson;

    @Inject
    public PersonaRepositoryFile(@Named("personaFileContents") String personaFileContents, Gson gson) {
        this.personaFileContents = personaFileContents;
        this.gson = gson;
    }

    @Override
    public Persona[] allPersonas() {
        RawPersona[] rawPersonas = gson.fromJson(personaFileContents, RawPersona[].class);
        Persona[] personas = new Persona[rawPersonas.length];

        for (int i = 0; i < rawPersonas.length ; i++) {
            personas[i] = Persona.mapFromRawPersona(rawPersonas[i]);
        }

        Arrays.sort(personas, new Comparator<Persona>() {
            @Override
            public int compare(Persona o1, Persona o2) {
                return o1.name.compareTo(o2.name);
            }
        });

        return personas;
    }
}
