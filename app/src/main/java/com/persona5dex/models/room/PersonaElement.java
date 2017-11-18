package com.persona5dex.models.room;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import com.persona5dex.models.Enumerations;

/**
 * Created by Rechee on 10/22/2017.
 */

@Entity(tableName = "personaElements",
    indices = {
        @Index("persona_id")
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
