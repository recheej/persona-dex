package com.persona5dex.models;

import androidx.room.Embedded;
import androidx.room.Ignore;

import com.persona5dex.models.room.Stats;

/**
 * Created by Rechee on 11/28/2017.
 */

public class PersonaDetailInfo {
    public String name;
    @Ignore
    public String arcanaName;
    public Enumerations.Arcana arcana;
    public int level;
    public String imageUrl;
    public String note;
    public boolean max;
    public int id;

    @Embedded
    public Stats stats;
}
