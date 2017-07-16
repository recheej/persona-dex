package com.example.rechee.persona5calculator.dagger;

import com.example.rechee.persona5calculator.models.Persona;
import com.example.rechee.persona5calculator.models.RawArcanaMap;
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
public class ApplicationRepositoryModule {

    @Persona5ApplicationScope
    @Provides
    PersonaRepository provideRepository(PersonaRepositoryFile fileRepository) {
        return fileRepository;
    }

    @Persona5ApplicationScope
    @Provides
    PersonaRepositoryFile repositoryFile(@Named("personaFileContents") String personaFileContents, @Named("arcanaMapFileContents") String arcanaMapFileContents, @Named("applicationGson") Gson gson){
        return new PersonaRepositoryFile(personaFileContents, arcanaMapFileContents, gson);
    }

    @Persona5ApplicationScope
    @Provides
    Persona[] personas(PersonaRepository repository) {
        return repository.allPersonas();
    }

    @Persona5ApplicationScope
    @Provides
    RawArcanaMap[] arcanaMaps(PersonaRepository repository) {
        return repository.rawArcanas();
    }

    @Persona5ApplicationScope
    @Provides
    @Named("applicationGson")
    public Gson gson() {
        return new Gson();
    }
}
