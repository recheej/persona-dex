package com.persona5dex.models.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import com.persona5dex.models.MainListPersona;
import com.persona5dex.models.PersonaDetailSkill;

import java.util.List;

/**
 * Created by reche on 1/4/2018.
 */

@Dao
public interface SkillDao {

    @Query("select * from skills where id = :skillID")
    LiveData<Skill> getSkill(Integer skillID);

    @Query(
            "select personaSkills.level_required as levelRequired, skills.name, skills.effect, " +
                    "skills.id as skillID from personaSkills " +
                    "inner join skills on skills.id = personaSkills.skill_id " +
                    "where personaSkills.persona_id = :personaID"
    )
    LiveData<List<PersonaDetailSkill>> getPersonaSkills(int personaID);

    @Transaction
    @Query(" select id, name, arcana, level, rare, dlc, party, personas.gameId from personaSkills\n" +
            "inner join personas on personas.id = personaSkills.persona_id\n" +
            "where personaSkills.skill_id = :skillID\n" +
            "order by name")
    LiveData<List<MainListPersona>> getPersonasWithSkillLiveData(int skillID);

    @Transaction
    @Query(" select id, name, arcana, level, rare, dlc, party, personas.gameId from personaSkills\n" +
            "inner join personas on personas.id = personaSkills.persona_id\n" +
            "where personaSkills.skill_id = :skillID\n" +
            "order by name")
    List<MainListPersona> getPersonasWithSkill(int skillID);
}
