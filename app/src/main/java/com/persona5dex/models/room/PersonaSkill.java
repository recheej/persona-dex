package com.persona5dex.models.room;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.annotation.NonNull;

import com.persona5dex.models.GameType;

/**
 * Created by Rechee on 10/22/2017.
 */

@Entity(tableName = "personaSkills",
        indices = {
            @Index(name = "ix_personaSkills_persona_id", value = {"persona_id"}),
            @Index(name = "ix_personaSkills_skill_id", value = {"skill_id"})
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

    @NonNull
    @ColumnInfo(defaultValue = "1")
    public GameType gameId = GameType.BASE;
}
