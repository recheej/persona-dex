package com.persona5dex.repositories;

import android.arch.lifecycle.LiveData;

import com.persona5dex.models.PersonaDetailSkill;
import com.persona5dex.models.room.PersonaDao;

import java.util.List;

/**
 * Created by Rechee on 11/28/2017.
 */

public class PersonaSkillsRoomRepository implements PersonaSkillsRepository {

    private final PersonaDao personaDao;

    public PersonaSkillsRoomRepository(PersonaDao personaDao){
        this.personaDao = personaDao;
    }

    @Override
    public LiveData<List<PersonaDetailSkill>> getPersonaSkillsForDetail(int personaID) {
        return personaDao.getPersonaSkillsForDetail(personaID);
    }
}
