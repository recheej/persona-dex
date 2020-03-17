package com.persona5dex.models.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.persona5dex.models.MainListPersona;
import com.persona5dex.models.PersonaDetailInfo;
import com.persona5dex.models.PersonaEdgeDisplay;
import com.persona5dex.models.PersonaForFusionService;
import com.persona5dex.models.PersonaShadowDetail;

import java.util.List;

/**
 * Created by Rechee on 10/22/2017.
 */

@Dao
public interface PersonaDao {
    @Transaction
    @Query("select id, name, arcana, level, rare, dlc, gameId from personas order by name")
    LiveData<List<MainListPersona>> getAllPersonasForMainListLiveData();

    @Query("select id, name, arcana, level, rare, dlc, gameId from personas order by name")
    List<MainListPersona> getAllPersonasForMainList();

    @Query("select id, name, level, endurance, agility, strength, magic, luck, imageUrl, note, max " +
            "from personas where id = :personaID " +
            "order by name")
    LiveData<PersonaDetailInfo> getDetailInfoForPersona(int personaID);

    @Query("select shadow_name as shadowName, isPrimary from personaShadowNames where persona_id = :personaID")
    LiveData<PersonaShadowDetail[]> getShadowsForPersona(int personaID);

    @Query("select id, arcana, name, level, rare, dlc, special from personas order by level")
    PersonaForFusionService[] getPersonasByLevel();

    @Transaction
    @Query("select id, name, arcana, level, rare, dlc from personas where dlc = 1")
    LiveData<List<MainListPersona>> getDLCPersonas();

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

    @Query("delete from personaFusions")
    void removeAllFusions();

    @Query("select name from personas where id = :personaID")
    LiveData<String> getPersonaName(int personaID);

    @Query("select special from personas where id = :personaID")
    LiveData<Integer> personaIsAdvanced(int personaID);
}
