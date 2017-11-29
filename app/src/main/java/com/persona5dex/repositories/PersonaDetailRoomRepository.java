package com.persona5dex.repositories;

import android.arch.lifecycle.LiveData;

import com.persona5dex.models.PersonaDetailInfo;
import com.persona5dex.models.room.PersonaDao;

/**
 * Created by Rechee on 11/28/2017.
 */

public class PersonaDetailRoomRepository implements PersonaDetailRepository {

    private final PersonaDao personaDao;

    public PersonaDetailRoomRepository(PersonaDao personaDao){
        this.personaDao = personaDao;
    }

    @Override
    public LiveData<PersonaDetailInfo> getDetailsForPersona(int personaID) {
        return personaDao.getDetailInfoForPersona(personaID);
    }
}
