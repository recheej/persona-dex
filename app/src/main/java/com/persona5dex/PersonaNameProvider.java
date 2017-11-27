package com.persona5dex;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.persona5dex.dagger.DaggerPersonaNameProviderComponent;
import com.persona5dex.dagger.NameProviderRepositoryModule;
import com.persona5dex.dagger.PersonaNameProviderComponent;
import com.persona5dex.models.Persona;
import com.persona5dex.models.room.PersonaDatabase;
import com.persona5dex.repositories.PersonaRepository;

import javax.inject.Inject;

/**
 * Created by Rechee on 7/3/2017.
 */

public class PersonaNameProvider extends ContentProvider {

    @Override
    public boolean onCreate() {
        return true;
    }

    @Inject
    PersonaRepository personaRepository;

    private Persona[] suggestions;

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        Context context = getContext();
        Persona5Application application = (Persona5Application) getContext().getApplicationContext();

        PersonaDatabase db = PersonaDatabase.getPersonaDatabase(application);
        PersonaNameProviderComponent component = DaggerPersonaNameProviderComponent.builder()
                .nameProviderRepositoryModule(new NameProviderRepositoryModule(context))
                .build();
        component.inject(this);

        if(this.suggestions == null){
            this.suggestions = personaRepository.allPersonas();
        }

        String query = uri.getLastPathSegment().toLowerCase();

        if(!query.equals("search_suggest_query")){
            Persona[] filteredPersonas = PersonaUtilities.filterPersonaByName(suggestions, query);
            return new PersonaNameCursor(filteredPersonas);
        }

        return new PersonaNameCursor(new Persona[0]);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
