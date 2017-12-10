package com.persona5dex.repositories;

import android.arch.lifecycle.LiveData;

import com.persona5dex.models.PersonaEdgeDisplay;
import com.persona5dex.models.PersonaStore;
import com.persona5dex.models.room.PersonaDao;

import java.util.List;

/**
 * Created by Rechee on 7/22/2017.
 */

public class PersonaDisplayEdgesRoomRepository implements  PersonaDisplayEdgesRepository{

    private final PersonaDao personaDao;

    public PersonaDisplayEdgesRoomRepository(PersonaDao personaDao){
        this.personaDao = personaDao;
    }

    public LiveData<List<PersonaEdgeDisplay>> getEdgesToPersona(int personaID) {
        return personaDao.getEdgesToPersona(personaID);
    }

    @Override
    public LiveData<List<PersonaEdgeDisplay>> getEdgesFromPersona(int personaID) {
        return personaDao.getEdgesFromPersona(personaID);
    }
}
