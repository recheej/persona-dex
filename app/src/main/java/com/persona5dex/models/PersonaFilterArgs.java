package com.persona5dex.models;

/**
 * Created by Rechee on 8/17/2017.
 */

public class PersonaFilterArgs {
    public Enumerations.Arcana arcana;
    public boolean dlcPersona;
    public int minLevel;
    public int maxLevel;
    public boolean basePersonas;
    public boolean royalPersonas;

    public PersonaFilterArgs() {
        minLevel = 1;
        maxLevel = 99;
        dlcPersona = true;
        arcana = Enumerations.Arcana.ANY;
        basePersonas = true;
        royalPersonas = true;
    }

    /**
     * @param minLevel    min persona level
     * @param maxLevel    maximum persona level
     * @param arcana      arcana to filter by
     * @param dlcPersona  True: include dlc persona
     */
    public PersonaFilterArgs(int minLevel, int maxLevel, Enumerations.Arcana arcana, boolean dlcPersona) {
        this.minLevel = minLevel;
        this.maxLevel = maxLevel;
        this.arcana = arcana;
        this.dlcPersona = dlcPersona;
    }

    /**
     * @param minLevel min persona level
     * @param maxLevel maximum persona level
     * @param arcana   arcana to filter by
     */
    public PersonaFilterArgs(int minLevel, int maxLevel, Enumerations.Arcana arcana) {
        this.minLevel = minLevel;
        this.maxLevel = maxLevel;
        this.arcana = arcana;
        this.dlcPersona = true;
    }
}
