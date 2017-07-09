package com.example.rechee.persona5calculator;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.rechee.persona5calculator.dagger.DaggerPersonaNameProviderComponent;
import com.example.rechee.persona5calculator.dagger.PersonaNameProviderComponent;
import com.example.rechee.persona5calculator.dagger.PersonaNameProviderContextModule;
import com.example.rechee.persona5calculator.models.PersonaSearchSuggestion;
import com.example.rechee.persona5calculator.repositories.PersonaSuggestionRepository;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * Created by Rechee on 7/3/2017.
 */

public class PersonaNameProvider extends ContentProvider {

    @Inject
    PersonaSuggestionRepository suggestionRepository;

    private PersonaSearchSuggestion[] suggestions;

    @Override
    public boolean onCreate() {
        PersonaNameProviderComponent component = DaggerPersonaNameProviderComponent
                .builder()
                .personaNameProviderContextModule(new PersonaNameProviderContextModule(getContext()))
                .build();
        component.inject(this);

        suggestions = suggestionRepository.allSuggestions();

        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        String query = uri.getLastPathSegment().toLowerCase();

        ArrayList<PersonaSearchSuggestion> filteredSuggestions = new ArrayList<>(this.suggestions.length);

        if(!query.equals("search_suggest_query")){
            for (PersonaSearchSuggestion suggestion: this.suggestions){
                String personaName = suggestion.name.toLowerCase();

                if(personaName.contains(query)){
                    filteredSuggestions.add(suggestion);
                }
            }
        }

        if(filteredSuggestions.size() == 0){
            return new PersonaNameCursor(new PersonaSearchSuggestion[0]);
        }

        return new PersonaNameCursor(filteredSuggestions.toArray(new PersonaSearchSuggestion[0]));
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
