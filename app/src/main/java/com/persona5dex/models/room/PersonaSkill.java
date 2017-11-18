package com.persona5dex.models.room;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by Rechee on 10/22/2017.
 */

@Entity(tableName = "personaSkills",
        indices = {
            @Index("skill_id")
        },
        primaryKeys = {"persona_id", "skill_id"},
        foreignKeys = {
                @ForeignKey(
                entity = Persona.class,
                parentColumns = "id",
                childColumns = "persona_id"
                ),
                @ForeignKey(
                        entity = Skill.class,
                        parentColumns = "id",
                        childColumns = "skill_id"
                )
        }
)
public class PersonaSkill {
    @ColumnInfo(name = "persona_id")
    public int personaID;

    @ColumnInfo(name = "skill_id")
    public int skillID;

    @ColumnInfo(name = "level_required")
    public int levelRequired;

    public PersonaSkill(int personaID, int skillID, int levelRequired) {
        this.personaID = personaID;
        this.skillID = skillID;
        this.levelRequired = levelRequired;
    }
}
