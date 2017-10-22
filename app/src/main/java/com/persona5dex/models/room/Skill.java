package com.persona5dex.models.room;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by Rechee on 10/22/2017.
 */

@Entity(tableName = "skills")
public class Skill {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String name;
    public String effect;
    public String element;
    public int cost;
    public String note;
}
