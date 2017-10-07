package com.example.rechee.persona5calculator.dagger;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.rechee.persona5calculator.PersonaFileUtilities;
import com.example.rechee.persona5calculator.PersonaUtilities;
import com.example.rechee.persona5calculator.R;
import com.example.rechee.persona5calculator.repositories.PersonaRepository;
import com.example.rechee.persona5calculator.repositories.PersonaRepositoryFile;
import com.google.gson.Gson;

import java.io.InputStream;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Rechee on 6/11/2017.
 */

@Module
public class ActivityContextModule {

    private final Context context;

    public ActivityContextModule(Context context){
        this.context = context;
    }

    @ActivityScope
    @Provides
    @Named("activityContext")
    public Context providesContext() {
        return context;
    }

    @ActivityScope
    @Provides
    @Named("transferSharedPreferences")
    SharedPreferences sharedPreferences(){
        return context.getSharedPreferences(PersonaUtilities.SHARED_PREF_TRANSFER_CONTENT, Context.MODE_PRIVATE);
    }

    @Provides
    @ActivityScope
    @Named("fusionSharedPreferences")
    SharedPreferences fusionSharedPreferences(){
        return context.getSharedPreferences(PersonaUtilities.SHARED_PREF_FUSIONS, Context.MODE_PRIVATE);
    }

    @Provides
    @ActivityScope
    @Named("dlcSharedPreferences")
    SharedPreferences dlcSharedPreferences(){
        return context.getSharedPreferences(PersonaUtilities.SHARED_PREF_DLC, Context.MODE_PRIVATE);
    }

    @Provides
    @ActivityScope
    @Named("defaultSharedPreferences")
    SharedPreferences defaultSharedPreferences(){
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Provides
    @ActivityScope
    @Named("dlcPrefKey")
    String dlcprefKey(){
        return context.getString(R.string.pref_key_dlc);
    }

    @Provides
    @ActivityScope
    @Named("rarePersonaInFusionKey")
    String rarePersonaInFusion(){
        return context.getString(R.string.pref_key_rarePersona);
    }

    @Provides
    @ActivityScope
    PersonaRepository provideRepository(Gson gson) {
        InputStream stream = context.getResources().openRawResource(R.raw.person_data);
        PersonaFileUtilities personaFileUtilities = new PersonaFileUtilities(gson);

        return new PersonaRepositoryFile(personaFileUtilities.allPersonas(stream));
    }
}
