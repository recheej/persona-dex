package com.persona5dex.repositories;

import androidx.lifecycle.LiveData;

import com.persona5dex.models.room.PersonaDao;
import com.persona5dex.models.room.PersonaElement;

import java.util.List;

/**
 * Created by Rechee on 11/28/2017.
 */

public class PersonaElementsRoomRepository implements PersonaElementsRepository {

    private final PersonaDao personaDao;

    public PersonaElementsRoomRepository(PersonaDao personaDao){
        this.personaDao = personaDao;
    }

    @Override
    public LiveData<List<PersonaElement>> getElementsForPersona(int personaID) {
        return personaDao.getElementsForPersona(personaID);
    }
}
