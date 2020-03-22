package com.persona5dex.models.room

import android.database.Cursor
import androidx.room.Dao
import androidx.room.Query
import com.persona5dex.searchSuggestions.SearchSuggestion

/**
 * Created by Rechee on 11/27/2017.
 */
@Dao
interface SearchSuggestionDao {
    @Query("""
        select id, name as lineOne, arcana as lineTwo, 1 as type from personas
        WHERE name LIKE :query
    """)
    fun getPersonaSearchSuggestions(query: String?): List<SearchSuggestion>

    @Query("""
        select id, name as lineOne, effect as lineTwo, 2 as type from skills
        WHERE name LIKE :query
    """)
    fun getSkillSearchSuggestions(query: String?): List<SearchSuggestion>
}