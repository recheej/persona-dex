package com.persona5dex.models.room;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.database.Cursor;

import com.persona5dex.models.MainListPersona;
import com.persona5dex.models.PersonaDetailInfo;
import com.persona5dex.models.PersonaDetailSkill;
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

    @Query("select name, arcanaName, level, endurance, agility, strength, magic, luck from personas where id = :personaID")
    LiveData<PersonaDetailInfo> getDetailInfoForPersona(int personaID);

    @Query("select id, arcana, arcanaName, name, level, rare, dlc, special from personas order by level")
    PersonaForFusionService[] getPersonasByLevel();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPersonaFusion(PersonaFusion personaFusion);

    @Query("select * from personaElements where persona_id = :personaID")
    LiveData<List<PersonaElement>> getElementsForPersona(int personaID);

    @Query(
            "select personaSkills.level_required as levelRequired, skills.name from personaSkills " +
                    "inner join skills on skills.id = personaSkills.skill_id " +
                    "where personaSkills.persona_id = :personaID"
    )
    LiveData<List<PersonaDetailSkill>> getPersonaSkillsForDetail(int personaID);

    @Query(
            "select p1.name as leftPersonaName, p2.name as rightPersonaName from personaFusions " +
                    "inner join persona as p1 on persona.id = personaFusions.result" +
                    "inner join persona as p2 on persona.id = personaFusions.persona_one " +
                    "where personaFusions.result = :personaID"
    )

    LiveData<List<PersonaEdgeDisplay>> getEdgesToPersona(int personaID);

    @Query(
            "select p1.name as leftPersonaName, p2.name as rightPersonaName from personaFusions " +
                    "inner join persona as p1 on persona.id = personaFusions.result" +
                    "inner join persona as p2 on persona.id = personaFusions.persona_one " +
                    "where personaFusions.result = :personaID"
    )
    LiveData<List<PersonaEdgeDisplay>> getEdgesFromPersona(int personaID);
}
