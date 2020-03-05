package com.persona5dex.models;


import androidx.room.Ignore;
import androidx.room.Relation;

import com.persona5dex.models.room.PersonaShadowName;

import java.util.List;

/**
 * Created by Rechee on 11/18/2017.
 */

public class MainListPersona {
    public int id;
    public String name;
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
}
