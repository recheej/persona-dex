package com.persona5dex.dagger.application;

import android.content.Context;
import android.content.SharedPreferences;

import com.persona5dex.Persona5Application;
import com.persona5dex.PersonaFileUtilities;
import com.persona5dex.PersonaUtilities;
import com.persona5dex.R;
import com.persona5dex.adapters.PersonaStoreGsonAdapter;
import com.persona5dex.dagger.activity.ActivityScope;
import com.persona5dex.models.PersonaStore;
import com.persona5dex.models.RawPersona;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.persona5dex.models.room.PersonaDatabase;

import java.io.InputStream;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Rechee on 6/11/2017.
 */

@Module
public class ApplicationContextModule {

    private final Context context;

    public ApplicationContextModule(Context context){
        this.context = context;
    }

    @Provides
    @ApplicationScope
    @Named("applicationContext")
    public Context providesContext() {
        return context;
    }

    @Provides
    @ApplicationScope
    Gson gson() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(PersonaStore.class, new PersonaStoreGsonAdapter());

        return builder.create();
    }

    @Provides
    @ApplicationScope
    @Named("dlcSharedPreferences")
    SharedPreferences dlcSharedPreferences(){
        return context.getSharedPreferences(PersonaUtilities.SHARED_PREF_DLC, Context.MODE_PRIVATE);
    }


    @Provides
    @ApplicationScope
    RawPersona[] rawPersonas(PersonaFileUtilities personaFileUtilities) {
        InputStream stream = context.getResources().openRawResource(R.raw.person_data);

        return personaFileUtilities.parseJsonFile(stream, RawPersona[].class);
    }

    @Provides
    @ApplicationScope
    PersonaDatabase personaDatabase() {
        return ((Persona5Application) context).getDatabase();
    }
}
