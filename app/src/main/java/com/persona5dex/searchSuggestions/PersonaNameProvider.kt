package com.persona5dex.searchSuggestions

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import com.persona5dex.dagger.contentProvider.ContentProviderComponent
import com.persona5dex.dagger.contentProvider.ContentProviderContextModule
import com.persona5dex.dagger.contentProvider.DaggerContentProviderComponent
import javax.inject.Inject

/**
 * Created by Rechee on 7/3/2017.
 */
class PersonaNameProvider : ContentProvider() {

    @Inject
    internal lateinit var searchSuggestionCursorProvider: SearchSuggestionCursorProvider

    private lateinit var component: ContentProviderComponent

    override fun onCreate(): Boolean {
        component = DaggerContentProviderComponent
                .builder()
                .contentProviderContextModule(ContentProviderContextModule(context))
                .build()
        component.inject(this)
        return true
    }

    override fun query(uri: Uri, projection: Array<String>?, selection: String?, selectionArgs: Array<String>?, sortOrder: String?): Cursor? {
        val pathSegments = uri.pathSegments
        val query = pathSegments[pathSegments.size - 1].toLowerCase()
        val suggestPath = pathSegments[0]
        val onlyPersonas = suggestPath == FUSION_SUGGEST_PATH
        return searchSuggestionCursorProvider.getSearchSuggestionCursor(String.format("%%%s%%", query), onlyPersonas, component.gameType())
    }

    override fun getType(uri: Uri): String? = null
    override fun insert(uri: Uri, values: ContentValues?): Uri? = null
    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int = 0
    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int =
            0

    companion object {
        private const val FUSION_SUGGEST_PATH = "fusion"
    }
}