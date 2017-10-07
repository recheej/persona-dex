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
    PersonaTransferRepository transferRepository(@Named("transferSharedPreferences") SharedPreferences sharedPreferences,
                                                 @Named("dlcSharedPreferences") SharedPreferences dlcSharedPreferences,
                                                 @Named("defaultSharedPreferences") SharedPreferences defaultSharedPreferences,
                                                 Gson gson,
                                                 @Named("dlcPrefKey") String dlcPrefKey,
                                                 @Named("rarePersonaInFusionKey") String rarePersonaInFusionKey){
        return new PersonaTransferRepositorySharedPref(sharedPreferences,
                dlcSharedPreferences,
                defaultSharedPreferences,
                gson,
                dlcPrefKey,
                rarePersonaInFusionKey);
    }

    @Provides
    @ActivityScope
    PersonaEdgesRepository edgesRepository(@Named("fusionSharedPreferences") SharedPreferences sharedPreferences, Gson gson){
        return new PersonaEdgesSharedPrefRepository(sharedPreferences, gson);
    }
}
