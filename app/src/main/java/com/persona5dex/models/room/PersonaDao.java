package com.persona5dex.models.room;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.database.Cursor;

import com.persona5dex.models.MainListPersona;

import java.util.List;

/**
 * Created by Rechee on 10/22/2017.
 */

@Dao
public interface PersonaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertPersonas(Persona... personas);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertPersonaElements(List<PersonaElement> personaElements);

    @Insert
    long insertPersona(Persona persona);

    @Query("select name, arcanaName, arcana, level, rare, dlc from personas")
    LiveData<List<MainListPersona>> getAllPersonasForMainList();

    @Insert
    long[] insertSkills(Skill... skills);

    @Insert
    long[] insertPersonaSkills(List<PersonaSkill> personaSkills);
}
