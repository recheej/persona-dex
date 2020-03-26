package com.persona5dex.searchSuggestions;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.persona5dex.dagger.contentProvider.ContentProviderContextModule;
import com.persona5dex.dagger.contentProvider.DaggerContentProviderComponent;
import com.persona5dex.models.room.SearchSuggestionDao;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by Rechee on 7/3/2017.
 */

public class PersonaNameProvider extends ContentProvider {

    public static final String FUSION_SUGGEST_PATH = "fusion";
    private SearchSuggestionDao dao;

    @Inject
    SearchSuggestionCursorProvider searchSuggestionCursorProvider;

    @Override
    public boolean onCreate() {
        DaggerContentProviderComponent
                .builder()
                .contentProviderContextModule(new ContentProviderContextModule(getContext()))
                .build()
                .inject(this);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final List<String> pathSegments = uri.getPathSegments();
        String query = pathSegments.get(pathSegments.size() - 1).toLowerCase();
        String suggestPath = pathSegments.get(0);

        boolean onlyPersonas = suggestPath.equals(FUSION_SUGGEST_PATH);
        return searchSuggestionCursorProvider.getSearchSuggestionCursor(String.format("%%%s%%", query), onlyPersonas);
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
