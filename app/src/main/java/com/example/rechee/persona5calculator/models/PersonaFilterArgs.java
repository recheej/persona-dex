package com.example.rechee.persona5calculator.models;

/**
 * Created by Rechee on 8/17/2017.
 */

public class PersonaFilterArgs {
    public boolean dlcPersona;
    public boolean rarePersona;
    public int minLevel;
    public int maxLevel;
    public Enumerations.Arcana arcana;
    public int arcanaOrdinal;

    public PersonaFilterArgs() {
        minLevel = 1;
        maxLevel = 99;
        rarePersona = true;
        dlcPersona = true;
    }

    /**
     * @param minLevel min persona level
     * @param maxLevel maximum persona level
     * @param arcana arcana to filter by
     * @param rarePersona True: include rare persona
     * @param dlcPersona True: include dlc persona
     */
    public PersonaFilterArgs(int minLevel, int maxLevel, Enumerations.Arcana arcana, boolean rarePersona, boolean dlcPersona){
        this.minLevel = minLevel;
        this.maxLevel = maxLevel;
        this.arcana = arcana;
        this.rarePersona = rarePersona;
        this.dlcPersona = dlcPersona;
    }

    /**
     * @param minLevel min persona level
     * @param maxLevel maximum persona level
     * @param arcana arcana to filter by
     */
    public PersonaFilterArgs(int minLevel, int maxLevel, Enumerations.Arcana arcana){
        this.minLevel = minLevel;
        this.maxLevel = maxLevel;
        this.arcana = arcana;
        this.rarePersona = true;
        this.dlcPersona = true;
    }
}
