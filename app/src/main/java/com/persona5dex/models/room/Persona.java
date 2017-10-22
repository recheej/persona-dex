package com.persona5dex.models.room;

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
    public int level;
    public String personality;

    //stats
    public int strength;
    public int magic;
    public int endurance;
    public int agility;
    public int luck;
}
