package com.persona5dex.models;

/**
 * Created by reche on 12/9/2017.
 */

public class PersonaForFusionService {
    private Enumerations.Arcana arcana;
    private String arcanaName;
    private int level;
    private String name;
    private boolean rare;
    private boolean special;
    private boolean dlc;
    private int id;

    public Enumerations.Arcana getArcana() {
        return arcana;
    }

    public void setArcana(Enumerations.Arcana arcana) {
        this.arcana = arcana;
    }

    public String getArcanaName() {
        return arcanaName;
    }

    public void setArcanaName(String arcanaName) {
        this.arcanaName = arcanaName;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isRare() {
        return rare;
    }

    public void setRare(boolean rare) {
        this.rare = rare;
    }

    public boolean isSpecial() {
        return special;
    }

    public void setSpecial(boolean special) {
        this.special = special;
    }

    public boolean isDlc() {
        return dlc;
    }

    public void setDlc(boolean dlc) {
        this.dlc = dlc;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
