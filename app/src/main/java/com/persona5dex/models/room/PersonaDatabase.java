package com.persona5dex.models.room;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.support.annotation.NonNull;

import com.huma.room_for_asset.RoomAsset;


/**
 * Created by Rechee on 10/22/2017.
 */

@Database(
        entities = {Persona.class, Skill.class, PersonaSkill.class, PersonaElement.class, SearchSuggestion.class},
        version = 2,
        exportSchema = false
)
@TypeConverters({PersonaTypeConverters.class})
public abstract class PersonaDatabase extends RoomDatabase {
    public abstract PersonaDao personaDao();
    public abstract SearchSuggestionDao searchSuggestionDao();

    private static PersonaDatabase INSTANCE;

//    static final Migration MIGRATION_2_3 = new Migration(2, 3) {
//        @Override
//        public void migrate(@NonNull SupportSQLiteDatabase database) {
//            database.execSQL("CREATE TABLE \"searchSuggestions\" ( `_id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, `suggest_text_1` TEXT, `suggest_text_2` TEXT, `suggest_intent_data` TEXT, `suggest_intent_extra_data` TEXT )");
//        }
//    };

    public static PersonaDatabase getPersonaDatabase(Context context){
        if(INSTANCE == null){
            INSTANCE = RoomAsset.databaseBuilder(context.getApplicationContext(),
                    PersonaDatabase.class, "persona-db.db")
                    .addMigrations(new Migration(1, 2) {
                        @Override
                        public void migrate(@NonNull SupportSQLiteDatabase database) {

                        }
                    }
                    )
                    .build();
        }

        return INSTANCE;
    }
}
