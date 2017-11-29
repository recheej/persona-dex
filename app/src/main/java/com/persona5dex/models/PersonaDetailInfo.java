package com.persona5dex.models;

import android.arch.persistence.room.Embedded;

import com.persona5dex.models.room.Stats;

/**
 * Created by Rechee on 11/28/2017.
 */

public class PersonaDetailInfo {
    public String name;
    public String arcanaName;
    public int level;

    @Embedded
    public Stats stats;
}
