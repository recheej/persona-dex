package com.example.rechee.persona5calculator.dagger;

import com.example.rechee.persona5calculator.repositories.PersonaRepository;
import com.example.rechee.persona5calculator.repositories.PersonaRepositoryFile;
import com.google.gson.Gson;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Rechee on 7/1/2017.
 */

@Module
public class NameProviderRepositoryModule {

    @PersonaNameProviderScope
    @Provides
    PersonaRepository provideRepository(@Named("personaFileContents") String personaFileContents, Gson gson) {
        return new PersonaRepositoryFile(personaFileContents, gson);
    }

    @Provides
    @PersonaNameProviderScope
    Gson gson() {
        return new Gson();
    }
}
