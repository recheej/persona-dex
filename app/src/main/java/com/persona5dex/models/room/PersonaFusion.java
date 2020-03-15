package com.persona5dex.models.room;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**
 * Created by reche on 12/8/2017.
 */

@Entity(tableName = "personaFusions",
        indices = {
                @Index(name = "ix_personaFusions_persona_one", value = {"persona_one"}),
                @Index(name = "ix_personaFusions_persona_two", value = {"persona_two"}),
                @Index(name = "ix_personaFusions_result", value = {"result"}),

        },
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

    @ColumnInfo(name = "persona_one")
    public int personaOneID;

    @ColumnInfo(name = "persona_two")
    public int personaTwoID;

    @ColumnInfo(name = "result")
    public int personaResultID;
}
