package com.persona5dex.searchSuggestions

import androidx.room.Entity
import com.persona5dex.models.GameType
import com.persona5dex.models.GameTypePersona

@Entity
open class SearchSuggestion(
        val id: Int,
        val lineOne: String,
        val lineTwo: String,
        val type: Int
)

@Entity
class PersonaSearchSuggestion(
        id: Int,
        lineOne: String,
        lineTwo: String,
        type: Int, override val gameId: GameType

) : SearchSuggestion(id, lineOne, lineTwo, type), GameTypePersona {
    override val name: String
        get() = lineOne
}