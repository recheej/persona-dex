package com.persona5dex.models.room;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import com.persona5dex.models.Enumerations;

/**
 * Created by Rechee on 10/22/2017.
 */

@Entity(tableName = "personaElements",
    foreignKeys = {
            @ForeignKey(
                    entity = Persona.class,
                    parentColumns = "id",
                    childColumns = "persona_id"
            )
    }
)
public class PersonaElement {

    @ColumnInfo(name = "persona_id")
    public int personaId;

    public Enumerations.ElementEffect physical;
    public Enumerations.ElementEffect gun;
    public Enumerations.ElementEffect fire;
    public Enumerations.ElementEffect ice;
    public Enumerations.ElementEffect electric;
    public Enumerations.ElementEffect wind;
    public Enumerations.ElementEffect psychic;
    public Enumerations.ElementEffect nuclear;
    public Enumerations.ElementEffect bless;
    public Enumerations.ElementEffect curse;
}
