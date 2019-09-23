package com.persona5dex.repositories;

import androidx.lifecycle.LiveData;

import com.persona5dex.models.room.PersonaElement;

import java.util.List;

/**
 * Created by Rechee on 11/28/2017.
 */

public interface PersonaElementsRepository {
    LiveData<List<PersonaElement>> getElementsForPersona(int personaID);
}
