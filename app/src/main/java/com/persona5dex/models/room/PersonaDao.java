package com.persona5dex.models.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;

/**
 * Created by Rechee on 10/22/2017.
 */

@Dao
public interface PersonaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public long[] insertPersonas(Persona... personas);

    public long insertPersona(Persona persona);
}
