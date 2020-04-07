package com.persona5dex.models.room

import androidx.room.Dao
import androidx.room.Query
import com.persona5dex.models.GameType
import com.persona5dex.searchSuggestions.PersonaSearchSuggestion
import com.persona5dex.searchSuggestions.SearchSuggestion

/**
 * Created by Rechee on 11/27/2017.
 */
@Dao
interface SearchSuggestionDao {
    @Query("""
        select id, name as lineOne, arcana as lineTwo, 1 as type, gameId from personas
        WHERE name LIKE :query
    """)
    fun getPersonaSearchSuggestions(query: String?): List<PersonaSearchSuggestion>

    @Query("""
        select id, name as lineOne, effect as lineTwo, 2 as type from skills
        WHERE name LIKE :query
    """)
    fun getSkillSearchSuggestions(query: String?): List<SearchSuggestion>

    @Query("""
        select personas.id, personas.name as lineOne, personaShadowNames.shadow_name as lineTwo, 3 as type from personaShadowNames
        inner join personas on personas.id = personaShadowNames.persona_id
        WHERE personaShadowNames.gameId = :gameType and shadow_name LIKE :query
    """)
    fun getShadowSuggestions(query: String?, gameType: GameType): List<SearchSuggestion>
}