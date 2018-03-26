package com.persona5dex.models.room;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by reche on 3/25/2018.
 */

@Entity(tableName = "personaShadowNames",
        indices = {
                @Index(name = "unique_shadows", value = {"persona_id", "shadow_name"}, unique = true),
                @Index(name = "ix_personaShadows_suggestion_id", value = {"suggestion_id"}),
        },
        foreignKeys = {
                @ForeignKey(
                        entity = Persona.class,
                        parentColumns = "id",
                        childColumns = "persona_id"
                ),
                @ForeignKey(
                        entity = SearchSuggestion.class,
                        parentColumns = "_id",
                        childColumns = "suggestion_id"
                )
        }
)
public class PersonaShadowName {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "persona_id")
    public int personaID;

    @ColumnInfo(name = "shadow_name")
    public String shadowName;

    public int primary;

    @ColumnInfo(name = "suggestion_id")
    public Integer suggestionID;
}
