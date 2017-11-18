package com.persona5dex.models.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

/**
 * Created by Rechee on 10/22/2017.
 */

@Database(
        entities = {Persona.class, Skill.class, PersonaSkill.class, PersonaElement.class},
        version = 1,
        exportSchema = false
)
@TypeConverters({PersonaTypeConverters.class})
public abstract class PersonaDatabase extends RoomDatabase {
    public abstract PersonaDao personaDao();

    private static PersonaDatabase INSTANCE;

    public static PersonaDatabase getPersonaDatabase(Context context){
        if(INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context,
                    PersonaDatabase.class, "perssona-db")
                    .build();
        }

        return INSTANCE;
    }

    public static PersonaDatabase getPersonaDatabase(Context context, RoomDatabase.Callback callback){
        if(INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context,
                    PersonaDatabase.class, "perssona-db")
                    .addCallback(callback)
                    .build();
        }

        return INSTANCE;
    }
}
