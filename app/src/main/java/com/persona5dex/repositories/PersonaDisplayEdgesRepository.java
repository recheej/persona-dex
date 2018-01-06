package com.persona5dex.repositories;

import android.arch.lifecycle.LiveData;

import com.persona5dex.models.PersonaEdgeDisplay;

import java.util.List;

/**
 * Created by reche on 12/9/2017.
 */

public interface PersonaDisplayEdgesRepository {
    LiveData<List<PersonaEdgeDisplay>> getEdgesToPersona(int personaID);
    LiveData<List<PersonaEdgeDisplay>> getEdgesFromPersona(int personaID);
    LiveData<String> getPersonaName(int personaID);

    boolean personaIsAdvanced(int personaID);
}
