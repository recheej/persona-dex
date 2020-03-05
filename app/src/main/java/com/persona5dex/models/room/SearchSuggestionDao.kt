package com.persona5dex.models.room

import android.database.Cursor
import androidx.room.Dao
import androidx.room.Query

/**
 * Created by Rechee on 11/27/2017.
 */
@Dao
interface SearchSuggestionDao {
    @Query("""
        select id as id, name as name, arcana as lineTwo, 1 as type from personas
        WHERE name LIKE :query
            union
        select id, name, effect, 2 from skills
        WHERE name LIKE :query
    """)
    fun getSuggestions(query: String?): Cursor
}