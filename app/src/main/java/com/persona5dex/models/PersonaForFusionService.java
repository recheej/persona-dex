package com.persona5dex.models;

import androidx.annotation.NonNull;

/**
 * Created by reche on 12/9/2017.
 */

public class PersonaForFusionService {
    private Enumerations.Arcana arcana;
    private int level;
    private String name;
    private boolean rare;
    private boolean special;
    private boolean dlc;
    private int id;
    private GameType gameType;

    @NonNull public Enumerations.Arcana getArcana() {
        return arcana;
    }

    public void setArcana(@NonNull Enumerations.Arcana arcana) {
        this.arcana = arcana;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @NonNull public String getName() {
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

    @NonNull public GameType getGameType() {
        return gameType;
    }

    public void setGameType(@NonNull GameType gameType) {
        this.gameType = gameType;
    }
}
