package com.persona5dex.dagger;

import android.content.Context;

import com.persona5dex.PersonaFileUtilities;
import com.persona5dex.R;
import com.persona5dex.repositories.PersonaRepository;
import com.persona5dex.repositories.PersonaRepositoryFile;
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

    @PersonaNameProviderScope
    @Provides
    PersonaRepository provideRepository(Gson gson) {
        InputStream stream = context.getResources().openRawResource(R.raw.person_data);
        PersonaFileUtilities personaFileUtilities = new PersonaFileUtilities(gson);

        return new PersonaRepositoryFile(personaFileUtilities.allPersonas(stream));
    }

    @Provides
    @PersonaNameProviderScope
    Gson gson() {
        return new Gson();
    }
}
