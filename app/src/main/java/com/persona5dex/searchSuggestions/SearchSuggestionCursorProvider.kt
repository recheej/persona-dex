package com.persona5dex.searchSuggestions

import android.app.SearchManager
import android.database.Cursor
import android.database.MatrixCursor
import android.provider.BaseColumns
import com.persona5dex.ArcanaNameProvider
import com.persona5dex.dagger.contentProvider.ContentProviderScope
import com.persona5dex.models.Enumerations
import com.persona5dex.models.room.PersonaDatabase
import javax.inject.Inject

@ContentProviderScope
class SearchSuggestionCursorProvider @Inject constructor(
        database: PersonaDatabase,
        private val arcanaNameProvider: ArcanaNameProvider
) {
    private val searchSuggestionDao = database.searchSuggestionDao()

    fun getSearchSuggestionCursor(query: String, onlyPersonas: Boolean): Cursor {
        val personaSearchSuggestions =
                searchSuggestionDao.getPersonaSearchSuggestions(query).map {
                    val arcana = Enumerations.Arcana.getArcana(it.lineTwo.toInt())
                    SearchSuggestion(
                            it.id,
                            it.lineOne,
                            arcanaNameProvider.getArcanaNameForDisplay(arcana),
                            it.type
                    )
                }

        val skillSearchSuggestions = searchSuggestionDao.getSkillSearchSuggestions(query)

        val suggestionList = if (onlyPersonas) personaSearchSuggestions else (personaSearchSuggestions + skillSearchSuggestions)
        val finalList = suggestionList
                .sortedBy { it.lineOne }
                .mapIndexed { index, searchSuggestion ->
                    arrayOf(
                            index.toLong(),
                            searchSuggestion.lineOne,
                            searchSuggestion.lineTwo,
                            searchSuggestion.id.toString(),
                            searchSuggestion.type.toString()
                    )
                }

        return MatrixCursor(columnNames, finalList.size).apply {
            finalList.forEach {
                addRow(it)
            }
        }
    }

    companion object {
        private val columnNames = arrayOf(
                BaseColumns._ID,
                SearchManager.SUGGEST_COLUMN_TEXT_1,
                SearchManager.SUGGEST_COLUMN_TEXT_2,
                SearchManager.SUGGEST_COLUMN_INTENT_DATA,
                SearchManager.SUGGEST_COLUMN_INTENT_EXTRA_DATA
        )
    }
}