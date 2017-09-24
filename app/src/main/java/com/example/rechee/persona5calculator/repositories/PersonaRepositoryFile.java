package com.example.rechee.persona5calculator.repositories;

import com.example.rechee.persona5calculator.PersonaFileUtilities;
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
    private final Persona[] allPersonas;

    @Inject
    public PersonaRepositoryFile(Persona[] allPersonas) {
        this.allPersonas = allPersonas;
    }

    @Override
    public Persona[] allPersonas() {
        Arrays.sort(allPersonas, new Comparator<Persona>() {
            @Override
            public int compare(Persona o1, Persona o2) {
                return o1.name.compareTo(o2.name);
            }
        });

        return allPersonas;
    }
}
