package com.persona5dex.repositories;

import com.persona5dex.PersonaFileUtilities;
import com.persona5dex.models.Persona;
import com.persona5dex.models.RawPersona;
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
