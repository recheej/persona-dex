package com.example.rechee.persona5calculator.repositories;

import com.example.rechee.persona5calculator.models.Persona;
import com.example.rechee.persona5calculator.models.PersonaSearchSuggestion;
import com.example.rechee.persona5calculator.models.RawPersona;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.Comparator;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by Rechee on 7/1/2017.
 */

public class PersonaSuggestionRepositoryFile implements PersonaSuggestionRepository {

    private final String personaFileContents;
    private final Gson gson;

    public PersonaSuggestionRepositoryFile(@Named("personaProviderFileContents") String personaFileContents, @Named("providerGson") Gson gson){
        this.personaFileContents = personaFileContents;
        this.gson = gson;
    }

    @Override
    public PersonaSearchSuggestion[] allSuggestions() {
        PersonaSearchSuggestion[] personaSearchSuggestions = gson.fromJson(personaFileContents, PersonaSearchSuggestion[].class);

        Arrays.sort(personaSearchSuggestions, new Comparator<PersonaSearchSuggestion>() {
            @Override
            public int compare(PersonaSearchSuggestion o1, PersonaSearchSuggestion o2) {
                return o1.name.compareTo(o2.name);
            }
        });

        return personaSearchSuggestions;
    }
}
