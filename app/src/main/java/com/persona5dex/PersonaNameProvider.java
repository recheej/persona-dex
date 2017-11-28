package com.persona5dex;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.persona5dex.models.room.PersonaDatabase;
import com.persona5dex.models.room.SearchSuggestionDao;

/**
 * Created by Rechee on 7/3/2017.
 */

public class PersonaNameProvider extends ContentProvider {

    private SearchSuggestionDao dao;

    @Override
    public boolean onCreate() {
        Persona5Application application = (Persona5Application) getContext().getApplicationContext();

        PersonaDatabase db = PersonaDatabase.getPersonaDatabase(application);
        dao = db.searchSuggestionDao();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        String query = uri.getLastPathSegment().toLowerCase();
        return dao.getSuggestions(String.format("%%%s%%", query));
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
