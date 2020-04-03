package com.persona5dex.models.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters


/**
 * Created by Rechee on 10/22/2017.
 */

@Database(
        entities = [
            Persona::class,
            Skill::class,
            PersonaSkill::class,
            PersonaElement::class,
            PersonaFusion::class,
            PersonaShadowName::class
        ],
        version = 4
)
@TypeConverters(PersonaTypeConverters::class)
abstract class PersonaDatabase : RoomDatabase() {
    abstract fun personaDao(): PersonaDao
    abstract fun skillDao(): SkillDao
    abstract fun searchSuggestionDao(): SearchSuggestionDao

    companion object {

        private var INSTANCE: PersonaDatabase? = null
        private const val DB_NAME = "persona-db.db"

        @JvmStatic
        fun getPersonaDatabase(context: Context): PersonaDatabase = INSTANCE ?: run {
            Room.databaseBuilder(
                    context,
                    PersonaDatabase::class.java,
                    DB_NAME
            )
                    .addMigrations(
                            MIGRATION_1_2,
                            MIGRATION_2_3
                    )
                    .createFromAsset("databases/$DB_NAME")
                    .fallbackToDestructiveMigration()
                    .build().also {
                        INSTANCE = it
                    }
        }
    }
}