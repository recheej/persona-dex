package com.persona5dex.models.room;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.persona5dex.models.GameType;
import com.persona5dex.models.GameTypePersona;

import org.jetbrains.annotations.NotNull;

/**
 * Created by reche on 3/25/2018.
 */

@Entity(tableName = "personaShadowNames",
        indices = {
                @Index(name = "unique_shadows", value = {"persona_id", "shadow_name", "gameId"}, unique = true)
        },
        foreignKeys = {
                @ForeignKey(
                        entity = Persona.class,
                        parentColumns = "id",
                        childColumns = "persona_id"
                )
        }
)
public class PersonaShadowName implements GameTypePersona {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "persona_id")
    public int personaID;

    @ColumnInfo(name = "shadow_name")
    public String shadowName;

    public int isPrimary;

    private GameType gameId;

    @Ignore
    public boolean isPrimaryShadow() {
        return isPrimary == 1;
    }

    @NotNull
    @Override
    public GameType getGameId() {
        return gameId;
    }

    public void setGameId(GameType gameId) {
        this.gameId = gameId;
    }

    @NotNull
    @Override
    @Ignore
    public String getName() {
        return shadowName;
    }
}
