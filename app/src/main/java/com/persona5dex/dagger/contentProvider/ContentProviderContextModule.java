package com.persona5dex.dagger.contentProvider;

import android.content.Context;

import com.google.gson.Gson;
import com.persona5dex.ArcanaNameProvider;
import com.persona5dex.models.room.PersonaDatabase;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Rechee on 7/1/2017.
 */

@Module
public class ContentProviderContextModule {

    private final Context context;

    public ContentProviderContextModule(Context context) {
        this.context = context;
    }

    @Provides
    @ContentProviderScope
    Gson gson() {
        return new Gson();
    }

    @Provides
    @ContentProviderScope
    ArcanaNameProvider arcanaNameProvider() {
        return new ArcanaNameProvider(context);
    }

    @Provides
    @ContentProviderScope
    PersonaDatabase personaDatabase() {
        return PersonaDatabase.getPersonaDatabase(context);
    }
}
