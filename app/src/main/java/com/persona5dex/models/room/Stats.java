package com.persona5dex.models.room;

import androidx.room.Entity;

/**
 * Created by Rechee on 11/18/2017.
 */

public class Stats {
    public int endurance;
    public int agility;
    public int strength;
    public int magic;
    public int luck;

    public Stats(int endurance, int agility, int strength, int magic, int luck) {
        this.endurance = endurance;
        this.agility = agility;
        this.strength = strength;
        this.magic = magic;
        this.luck = luck;
    }
}
