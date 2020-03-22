package com.persona5dex.searchSuggestions

import androidx.room.Entity

@Entity
class SearchSuggestion(
        val id: Int,
        val lineOne: String,
        val lineTwo: String,
        val type: Int
)