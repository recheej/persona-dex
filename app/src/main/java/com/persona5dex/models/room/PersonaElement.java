package com.persona5dex.models.room;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.persona5dex.models.Enumerations;

/**
 * Created by Rechee on 10/22/2017.
 */

@Entity(tableName = "personaElements",
    indices = {
        @Index(name = "ix_personaElements_persona_id", value = {"persona_id"})
    },
    foreignKeys = {
            @ForeignKey(
                    entity = Persona.class,
                    parentColumns = "id",
                    childColumns = "persona_id"
            )
    }
)
public class PersonaElement {
    public PersonaElement(int personaId, Enumerations.Element element, Enumerations.ElementEffect effect) {
        this.personaId = personaId;
        this.element = element;
        this.effect = effect;
    }

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "persona_id")
    public int personaId;

    public Enumerations.Element element;
    public Enumerations.ElementEffect effect;
}
