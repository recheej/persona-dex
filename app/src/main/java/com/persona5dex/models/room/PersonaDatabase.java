package com.persona5dex.models.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

/**
 * Created by Rechee on 10/22/2017.
 */

@Database(
        entities = {Persona.class, Skill.class, PersonaSkill.class, PersonaElement.class},
        version = 1
)
public abstract class PersonaDatabase extends RoomDatabase {
    public abstract PersonaDao personaDao();
}
