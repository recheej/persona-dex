package com.persona5dex.repositories;

import androidx.lifecycle.LiveData;

import com.persona5dex.models.GameType;
import com.persona5dex.models.PersonaDetailInfo;
import com.persona5dex.models.PersonaShadowDetail;
import com.persona5dex.models.room.PersonaDao;

/**
 * Created by Rechee on 11/28/2017.
 */

public class PersonaDetailRoomRepository implements PersonaDetailRepository {

    private final PersonaDao personaDao;
    private final GameType gameType;

    public PersonaDetailRoomRepository(PersonaDao personaDao, GameType gameType) {
        this.personaDao = personaDao;
        this.gameType = gameType;
    }

    @Override
    public LiveData<PersonaDetailInfo> getDetailsForPersona(int personaID) {
        return personaDao.getDetailInfoForPersona(personaID);
    }

    @Override
    public LiveData<PersonaShadowDetail[]> getShadowsForPersona(int id) {
        return personaDao.getShadowsForPersona(id, gameType);
    }
}
