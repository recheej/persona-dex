package com.persona5dex.models.room;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

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
    public int level;
    public String personality;

    public boolean special;
    public boolean max;
    public boolean dlc;
    public boolean rare;

    public String note;


    @Embedded
    public Stats stats;
}
