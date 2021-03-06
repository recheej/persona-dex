package com.persona5dex.repositories;

import androidx.lifecycle.LiveData;

import com.persona5dex.models.PersonaEdgeDisplay;

import java.util.List;

/**
 * Created by reche on 12/9/2017.
 */

public interface PersonaDisplayEdgesRepository {
    LiveData<List<PersonaEdgeDisplay>> getEdgesToPersona(int personaID);
    LiveData<List<PersonaEdgeDisplay>> getEdgesFromPersona(int personaID);
    LiveData<Integer> personaIsAdvanced(int personaID);
}
