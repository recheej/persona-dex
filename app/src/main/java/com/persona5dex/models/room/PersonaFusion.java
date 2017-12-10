package com.persona5dex.models.room;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by reche on 12/8/2017.
 */

@Entity(tableName = "personaFusions",
        foreignKeys = {
                @ForeignKey(
                        entity = Persona.class,
                        parentColumns = "id",
                        childColumns = "persona_one"
                ),
                @ForeignKey(
                        entity = Persona.class,
                        parentColumns = "id",
                        childColumns = "persona_two"
                ),
                @ForeignKey(
                        entity = Persona.class,
                        parentColumns = "id",
                        childColumns = "result"
                )
        }
)
public class PersonaFusion {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "persona_one")
    public int personaOneID;

    @ColumnInfo(name = "persona_two")
    public int personaTwoID;

    @ColumnInfo(name = "result")
    public int personaResultID;
}
