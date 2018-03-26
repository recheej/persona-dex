package com.persona5dex.models.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;
import android.database.Cursor;

/**
 * Created by Rechee on 11/27/2017.
 */

@Dao
public interface SearchSuggestionDao {
    @Query("select * from searchSuggestions where suggest_text_1 like :query\n" +
            "union\n" +
            "select searchSuggestions.* from personaShadowNames\n" +
            "inner join searchSuggestions on personaShadowNames.suggestion_id = searchSuggestions._id\n" +
            "where personaShadowNames.shadow_name like :query")
    Cursor getSuggestions(String query);
}
