package com.persona5dex.models.room;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

/**
 * Created by Rechee on 11/27/2017.
 */

@Entity(
        tableName = "searchSuggestions"

)
public class SearchSuggestion {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    public int id;

    @ColumnInfo(name = "suggest_text_1")
    public String suggestionColumnTextOne;

    @ColumnInfo(name = "suggest_text_2")
    public String suggestionColumnTextTwo;

    @ColumnInfo(name = "suggest_intent_data")
    public String suggestColumnIntentData;

    @ColumnInfo(name = "suggest_intent_extra_data")
    public String suggestColumnIntentExtraData;
}
