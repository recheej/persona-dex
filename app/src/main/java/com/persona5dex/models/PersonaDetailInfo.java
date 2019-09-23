package com.persona5dex.models;

import androidx.room.Embedded;

import com.persona5dex.models.room.Stats;

/**
 * Created by Rechee on 11/28/2017.
 */

public class PersonaDetailInfo {
    public String name;
    public String arcanaName;
    public int level;
    public String imageUrl;
    public String note;
    public boolean max;
    public int id;

    @Embedded
    public Stats stats;
}
