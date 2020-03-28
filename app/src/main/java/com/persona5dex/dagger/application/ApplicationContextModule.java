package com.persona5dex.dagger.application;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.work.WorkManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.persona5dex.ArcanaNameProvider;
import com.persona5dex.Constants;
import com.persona5dex.Persona5Application;
import com.persona5dex.PersonaUtilities;
import com.persona5dex.fusionService.FusionChartService;
import com.persona5dex.fusionService.FusionChartServiceFactory;
import com.persona5dex.models.GameType;
import com.persona5dex.models.room.PersonaDao;
import com.persona5dex.models.room.PersonaDatabase;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Rechee on 6/11/2017.
 */

@Module
public class ApplicationContextModule {

    private final Context context;

    public ApplicationContextModule(Context context) {
        this.context = context;
    }

    @Provides
    @Singleton
    @Named("applicationContext")
    public Context providesContext() {
        return context;
    }

    @Provides
    @Singleton
    Gson gson() {
        return new GsonBuilder().create();
    }

    @Provides
    @Singleton
    @Named("dlcSharedPreferences")
    SharedPreferences dlcSharedPreferences() {
        return context.getSharedPreferences(PersonaUtilities.SHARED_PREF_DLC, Context.MODE_PRIVATE);
    }

    @Provides
    @Singleton
    PersonaDatabase personaDatabase() {
        return ((Persona5Application) context).getDatabase();
    }

    @Provides
    @Singleton
    @Named("defaultSharedPreferences")
    SharedPreferences defaultSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Provides
    @Singleton
    WorkManager providesWorkManager() {
        return WorkManager.getInstance(context);
    }

    @Provides
    @Singleton
    PersonaDao providesPersonaDao(PersonaDatabase personaDatabase) {
        return personaDatabase.personaDao();
    }

    @Provides
    GameType gameType(@Named("defaultSharedPreferences") SharedPreferences sharedPreferences) {
        final int gameTypeInt = sharedPreferences.getInt(Constants.SHARED_PREF_KEY_GAME_TYPE, GameType.BASE.getValue());
        return GameType.getGameType(gameTypeInt);
    }

    @Provides
    FusionChartService providesFusionChartService(FusionChartServiceFactory fusionChartServiceFactory, GameType gameType) {
        return fusionChartServiceFactory.getFusionChartService(gameType);
    }

    @Provides
    ArcanaNameProvider arcanaNameProvider(@Named("applicationContext") Context context) {
        return new ArcanaNameProvider(context);
    }
}
