package com.persona5dex.models.room;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.persona5dex.models.MainListPersona;
import com.persona5dex.models.PersonaDetailInfo;
import com.persona5dex.models.PersonaEdgeDisplay;
import com.persona5dex.models.PersonaForFusionService;

import java.util.List;

/**
 * Created by Rechee on 10/22/2017.
 */

@Dao
public interface PersonaDao {
    @Query("select id, name, arcanaName, arcana, level, rare, dlc from personas")
    LiveData<List<MainListPersona>> getAllPersonasForMainList();

    @Query("select name, arcanaName, level, endurance, agility, strength, magic, luck, imageUrl from personas where id = :personaID")
    LiveData<PersonaDetailInfo> getDetailInfoForPersona(int personaID);

    @Query("select id, arcana, arcanaName, name, level, rare, dlc, special from personas order by level")
    PersonaForFusionService[] getPersonasByLevel();

    @Query("select * from personas where dlc = 1")
    LiveData<List<Persona>> getDLCPersonas();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPersonaFusion(PersonaFusion personaFusion);

    @Query("select * from personaElements where persona_id = :personaID")
    LiveData<List<PersonaElement>> getElementsForPersona(int personaID);

    @Query(
            "select p1.name as leftPersonaName, p1.id as leftPersonaID, " +
                    "p2.name as rightPersonaName, p2.id as rightPersonaID, " +
                    "null as resultPersonaName, 0 as resultPersonaID " +
                    "from personaFusions " +
                    "inner join personas as p1 on p1.id = personaFusions.persona_one " +
                    "inner join personas as p2 on p2.id = personaFusions.persona_two " +
                    "where personaFusions.result = :personaID"
    )
    LiveData<List<PersonaEdgeDisplay>> getEdgesToPersona(int personaID);

    @Query(
            "select p1.name as leftPersonaName, p1.id as leftPersonaID, " +
                    "p2.name as rightPersonaName, p2.id as rightPersonaID, " +
                    "p3.name as resultPersonaName, p3.id as resultPersonaID " +
                    "from personaFusions " +
                    "inner join personas as p1 on p1.id = personaFusions.persona_one " +
                    "inner join personas as p2 on p2.id = personaFusions.persona_two " +
                    "inner join personas as p3 on p3.id = personaFusions.result " +
                    "where personaFusions.persona_one = :personaID or personaFusions.persona_two == :personaID"
    )
    LiveData<List<PersonaEdgeDisplay>> getEdgesFromPersona(int personaID);

    @Query("select name from personas where id = :personaID")
    LiveData<String> getPersonaName(int personaID);


}
