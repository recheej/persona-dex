package com.persona5dex.repositories;

import android.arch.lifecycle.LiveData;

import com.persona5dex.models.PersonaDetailInfo;

/**
 * Created by Rechee on 11/28/2017.
 */

public interface PersonaDetailRepository {
    LiveData<PersonaDetailInfo> getDetailsForPersona(int personaID);
}
