package com.example.rechee.persona5calculator;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.rechee.persona5calculator.dagger.DaggerPersonaNameProviderComponent;
import com.example.rechee.persona5calculator.dagger.PersonaNameProviderComponent;
import com.example.rechee.persona5calculator.dagger.PersonaNameProviderContextModule;
import com.example.rechee.persona5calculator.models.Persona;
import com.example.rechee.persona5calculator.models.PersonaSearchSuggestion;
import com.example.rechee.persona5calculator.repositories.PersonaSuggestionRepository;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * Created by Rechee on 7/3/2017.
 */

public class PersonaNameProvider extends ContentProvider {

    @Override
    public boolean onCreate() {
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Persona5Application application = (Persona5Application) getContext().getApplicationContext();

        Persona[] suggestions = application.getAllPersonas();

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
