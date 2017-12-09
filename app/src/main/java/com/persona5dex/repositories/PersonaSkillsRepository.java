package com.persona5dex.repositories;

import android.arch.lifecycle.LiveData;

import com.persona5dex.models.PersonaDetailSkill;
import com.persona5dex.models.room.PersonaElement;

import java.util.List;

/**
 * Created by Rechee on 11/28/2017.
 */

public interface PersonaSkillsRepository {
    LiveData<List<PersonaDetailSkill>> getPersonaSkillsForDetail(int personaID);
}
