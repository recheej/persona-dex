package com.persona5dex.models;


import androidx.room.Ignore;
import androidx.room.Relation;

import com.persona5dex.models.room.PersonaShadowName;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Created by Rechee on 11/18/2017.
 */

public class MainListPersona implements GameTypePersona {
    public int id;
    private String name;
    public Enumerations.Arcana arcana;
    public int level;
    public boolean rare;
    public boolean dlc;
    public GameType gameId;

    @Relation(parentColumn = "id", entityColumn = "persona_id")
    public List<PersonaShadowName> personaShadowNames;

    @Ignore
    public PersonaShadowName getPrimaryShadow() {
        if(personaShadowNames == null || personaShadowNames.size() == 0){
            return null;
        }

        for (PersonaShadowName personaShadowName : personaShadowNames) {
            if(personaShadowName.isPrimaryShadow()){
                return personaShadowName;
            }
        }

        return null;
    }

    @Ignore
    public String getNameForDisplay() {
        PersonaShadowName primaryShadow = this.getPrimaryShadow();
        if(primaryShadow == null){
            return this.name;
        }

        return String.format("%s (%s)", this.name, primaryShadow.shadowName);
    }

    @NotNull
    @Override
    public GameType getGameId() {
        return gameId;
    }

    @NotNull
    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
