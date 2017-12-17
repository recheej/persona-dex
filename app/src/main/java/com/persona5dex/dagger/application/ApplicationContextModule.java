package com.persona5dex.dagger.application;

import android.content.Context;

import com.persona5dex.Persona5Application;
import com.persona5dex.PersonaFileUtilities;
import com.persona5dex.R;
import com.persona5dex.adapters.PersonaStoreGsonAdapter;
import com.persona5dex.models.PersonaStore;
import com.persona5dex.models.RawPersona;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.persona5dex.models.RawSkill;
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
    RawPersona[] rawPersonas(Gson gson) {
        InputStream stream = context.getResources().openRawResource(R.raw.person_data);
        PersonaFileUtilities utilities = new PersonaFileUtilities(gson);

        return utilities.parseJsonFile(stream, RawPersona[].class);
    }

    @Provides
    @ApplicationScope
    RawSkill[] rawSkills(Gson gson){
        InputStream stream = context.getResources().openRawResource(R.raw.skill_data);
        PersonaFileUtilities utilities = new PersonaFileUtilities(gson);

        return utilities.parseJsonFile(stream, RawSkill[].class);
    }

    @Provides
    @ApplicationScope
    PersonaDatabase personaDatabase() {
        return Persona5Application.getPersonaDatabase(context);
    }
}
