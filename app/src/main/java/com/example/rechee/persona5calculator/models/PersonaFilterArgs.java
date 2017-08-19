package com.example.rechee.persona5calculator.models;

/**
 * Created by Rechee on 8/17/2017.
 */

public class PersonaFilterArgs {
    public int minLevel;
    public int maxLevel;
    public Enumerations.Arcana arcana;

    public PersonaFilterArgs() {
        minLevel = 1;
        maxLevel = 99;
    }
}
