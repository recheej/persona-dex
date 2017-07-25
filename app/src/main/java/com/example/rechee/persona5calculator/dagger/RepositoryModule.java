package com.example.rechee.persona5calculator.dagger;

import android.content.SharedPreferences;

import com.example.rechee.persona5calculator.repositories.PersonaRepository;
import com.example.rechee.persona5calculator.repositories.PersonaRepositoryFile;
import com.example.rechee.persona5calculator.repositories.PersonaTransferRepository;
import com.example.rechee.persona5calculator.repositories.PersonaTransferRepositorySharedPref;
import com.google.gson.Gson;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Rechee on 7/1/2017.
 */

@Module
public class RepositoryModule {

    @ViewModelScope
    @Provides
    PersonaRepository provideRepository(PersonaRepositoryFile fileRepository) {
        return fileRepository;
    }

    @ViewModelScope
    @Provides
    PersonaRepositoryFile repositoryFile(@Named("personaFileContents") String personaFileContents, @Named("applicationGson") Gson gson){
        return new PersonaRepositoryFile(personaFileContents, gson);
    }

    @ViewModelScope
    @Provides
    PersonaTransferRepository transferRepository(@Named("transferSharedPreferences") SharedPreferences sharedPreferences, @Named("applicationGson") Gson gson){
        return new PersonaTransferRepositorySharedPref(sharedPreferences, gson);
    }
}
