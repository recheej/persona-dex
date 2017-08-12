package com.example.rechee.persona5calculator.dagger;

import android.content.SharedPreferences;

import com.example.rechee.persona5calculator.repositories.PersonaEdgesRepository;
import com.example.rechee.persona5calculator.repositories.PersonaEdgesSharedPrefRepository;
import com.example.rechee.persona5calculator.repositories.PersonaRepository;
import com.example.rechee.persona5calculator.repositories.PersonaRepositoryFile;
import com.example.rechee.persona5calculator.repositories.PersonaTransferRepository;
import com.example.rechee.persona5calculator.repositories.PersonaTransferRepositorySharedPref;
import com.google.gson.Gson;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Rechee on 7/3/2017.
 */

@Module
public class ViewModelRepositoryModule {
    @Provides
    @ActivityScope
    PersonaRepository provideRepository(@Named("personaFileContents") String personaFileContents, Gson gson) {
        return new PersonaRepositoryFile(personaFileContents, gson);
    }

    @Provides
    @ActivityScope
    Gson gson() {
        return new Gson();
    }

    @Provides
    @ActivityScope
    PersonaTransferRepository transferRepository(@Named("transferSharedPreferences") SharedPreferences sharedPreferences, Gson gson){
        return new PersonaTransferRepositorySharedPref(sharedPreferences, gson);
    }

    @Provides
    @ActivityScope
    PersonaEdgesRepository edgesRepository(@Named("fusionSharedPreferences") SharedPreferences sharedPreferences,  @Named("fusionCommonPreferences") SharedPreferences commonPreferences, Gson gson){
        return new PersonaEdgesSharedPrefRepository(sharedPreferences, commonPreferences, gson);
    }
}
