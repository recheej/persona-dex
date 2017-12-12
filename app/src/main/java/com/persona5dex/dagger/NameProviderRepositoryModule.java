package com.persona5dex.dagger;

import android.content.Context;

import com.persona5dex.PersonaFileUtilities;
import com.persona5dex.R;
import com.google.gson.Gson;

import java.io.InputStream;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Rechee on 7/1/2017.
 */

@Module
public class NameProviderRepositoryModule {

    private final Context context;

    public NameProviderRepositoryModule(Context context){
        this.context = context;
    }

    @Provides
    @PersonaNameProviderScope
    Gson gson() {
        return new Gson();
    }
}
