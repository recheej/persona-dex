package com.persona5dex.models.room;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.persona5dex.models.Enumerations;
import com.persona5dex.models.GameType;

/**
 * Created by Rechee on 10/22/2017.
 */

@Entity(tableName = "personas")
public class Persona {
    @PrimaryKey
    public int id;
    public String name;
    public Enumerations.Arcana arcana;
    public String arcanaName;
    public int level;

    @Nullable
    public String personality;

    public boolean special;
    public boolean max;
    public boolean dlc;
    public boolean rare;

    public String note;

    public String imageUrl;

    @Embedded
    public Stats stats;

    @NonNull
    @ColumnInfo(defaultValue = "1")
    public GameType gameId = GameType.BASE;
}
