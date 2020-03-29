package com.persona5dex.repositories;

import androidx.lifecycle.LiveData;

import com.persona5dex.models.MainListPersona;
import com.persona5dex.models.PersonaDetailSkill;
import com.persona5dex.models.room.Skill;

import java.util.List;

/**
 * Created by Rechee on 11/28/2017.
 */

public interface PersonaSkillsRepository {
    LiveData<List<PersonaDetailSkill>> getPersonaSkillsForDetail(int personaID);
    LiveData<Skill> getSkill(Integer skillID);
    LiveData<List<MainListPersona>> getPersonasWithSkillLiveData(int skillID);
    List<MainListPersona> getPersonasWithSkill(int skillID);
}
