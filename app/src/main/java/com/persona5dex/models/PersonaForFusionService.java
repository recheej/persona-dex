package com.persona5dex.models;

import androidx.annotation.NonNull;

import java.util.Objects;

/**
 * Created by reche on 12/9/2017.
 */

public class PersonaForFusionService implements GameTypePersona {
    private Enumerations.Arcana arcana;
    private int level;
    private String name;
    private boolean rare;
    private boolean special;
    private boolean dlc;
    private int id;
    private GameType gameId;

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

    @Override
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

    @Override
    @NonNull public GameType getGameId() {
        return gameId;
    }

    public void setGameId(@NonNull GameType gameType) {
        this.gameId = gameType;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        PersonaForFusionService that = (PersonaForFusionService) o;
        return level == that.level &&
                rare == that.rare &&
                special == that.special &&
                dlc == that.dlc &&
                id == that.id &&
                arcana == that.arcana &&
                name.equals(that.name) &&
                gameId == that.gameId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(arcana, level, name, rare, special, dlc, id, gameId);
    }
}
