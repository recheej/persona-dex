package com.example.rechee.persona5calculator.dagger;

import android.content.Context;

import com.example.rechee.persona5calculator.models.PersonaSearchSuggestion;
import com.example.rechee.persona5calculator.repositories.PersonaSuggestionRepository;
import com.example.rechee.persona5calculator.repositories.PersonaSuggestionRepositoryFile;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.Comparator;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Rechee on 7/1/2017.
 */

@Module
public class PersonaNameProviderDataModule {

    @PersonaNameProviderScope
    @Provides
    public PersonaSuggestionRepository suggestionRepository(@Named("personaProviderFileContents") String personaFileContents, @Named("providerGson") Gson gson) {
        return new PersonaSuggestionRepositoryFile(personaFileContents, gson);
    }

    @PersonaNameProviderScope
    @Provides
    @Named("providerGson")
    public Gson gson() {
        return new Gson();
    }
}
