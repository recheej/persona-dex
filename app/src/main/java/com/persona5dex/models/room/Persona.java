package com.persona5dex.models.room;

import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

import com.persona5dex.models.Enumerations;

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
    @NonNull
    public int level;
    public String personality;

    @NonNull
    public boolean special;
    @NonNull
    public boolean max;
    @NonNull
    public boolean dlc;
    @NonNull
    public boolean rare;

    public String note;

    public String imageUrl;

    @Embedded
    public Stats stats;
}
