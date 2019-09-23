package com.persona5dex.repositories;

import androidx.lifecycle.LiveData;

import com.persona5dex.models.PersonaDetailInfo;
import com.persona5dex.models.PersonaShadowDetail;

/**
 * Created by Rechee on 11/28/2017.
 */

public interface PersonaDetailRepository {
    LiveData<PersonaDetailInfo> getDetailsForPersona(int personaID);

    LiveData<PersonaShadowDetail[]> getShadowsForPersona(int id);
}
