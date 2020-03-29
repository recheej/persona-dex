package com.persona5dex.repositories;

import androidx.lifecycle.LiveData;

import com.persona5dex.models.MainListPersona;
import com.persona5dex.models.PersonaDetailSkill;
import com.persona5dex.models.room.Skill;
import com.persona5dex.models.room.SkillDao;

import java.util.List;

/**
 * Created by Rechee on 11/28/2017.
 */

public class PersonaSkillsRoomRepository implements PersonaSkillsRepository {

    private final SkillDao skillDao;

    public PersonaSkillsRoomRepository(SkillDao skillDao) {
        this.skillDao = skillDao;
    }

    @Override
    public LiveData<List<PersonaDetailSkill>> getPersonaSkillsForDetail(int personaID) {
        return skillDao.getPersonaSkills(personaID);
    }

    @Override
    public LiveData<Skill> getSkill(Integer skillID) {
        return skillDao.getSkill(skillID);
    }

    @Override
    public LiveData<List<MainListPersona>> getPersonasWithSkillLiveData(int skillID) {
        return skillDao.getPersonasWithSkillLiveData(skillID);
    }

    @Override
    public List<MainListPersona> getPersonasWithSkill(int skillID) {
        return skillDao.getPersonasWithSkill(skillID);
    }
}
