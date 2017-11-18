package com.persona5dex.models;

/**
 * Created by Rechee on 11/18/2017.
 */

public class RawSkill {
    public String name;
    public String effect;
    public String element;
    public PersonaForSkill[] personas;
    public int cost;
    public String note;

    public class PersonaForSkill {
        public String name;
        public int levelRequired;
    }
}

