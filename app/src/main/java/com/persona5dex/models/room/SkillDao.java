package com.persona5dex.models.room;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

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
            "select personaSkills.level_required as levelRequired, skills.name, skills.id as skillID from personaSkills " +
                    "inner join skills on skills.id = personaSkills.skill_id " +
                    "where personaSkills.persona_id = :personaID"
    )
    LiveData<List<PersonaDetailSkill>> getPersonaSkills(int personaID);
}
