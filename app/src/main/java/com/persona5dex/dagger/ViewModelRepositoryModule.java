package com.persona5dex.dagger;

import android.content.SharedPreferences;

import com.persona5dex.models.room.PersonaDatabase;
import com.persona5dex.repositories.PersonaEdgesRepository;
import com.persona5dex.repositories.PersonaEdgesSharedPrefRepository;
import com.persona5dex.repositories.PersonaRepository;
import com.persona5dex.repositories.PersonaRepositoryFile;
import com.persona5dex.repositories.PersonaTransferRepository;
import com.persona5dex.repositories.PersonaTransferRepositorySharedPref;
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
    PersonaEdgesRepository edgesRepository(@Named("fusionSharedPreferences") SharedPreferences sharedPreferences, Gson gson, PersonaDatabase database){
        return new PersonaEdgesSharedPrefRepository(sharedPreferences, gson, database);
    }
}
