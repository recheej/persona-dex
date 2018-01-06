package com.persona5dex.models;

/**
 * Created by reche on 12/9/2017.
 */

public class PersonaDetailSkill {
    public String name;
    public int levelRequired;
    public String effect;
    public int skillID;

    public PersonaDetailSkill(String name, int levelRequired, String effect, int skillID) {
        this.name = name;
        this.levelRequired = levelRequired;
        this.effect = effect;
        this.skillID = skillID;
    }
}
