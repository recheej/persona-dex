package com.persona5dex.repositories;

import com.persona5dex.models.Persona;
import com.persona5dex.models.PersonaForFusionService;
import com.persona5dex.models.PersonaStore;

/**
 * Created by Rechee on 7/22/2017.
 */

public interface PersonaEdgesRepository {
    void addPersonaEdges(PersonaStore personaStore);
    void markFinished();
    void markInit();
    PersonaStore getEdgesForPersona(int personaID);
    boolean edgesStored();
    int getEdgesVersionCode();
    void updateEdgesVersion(int newVersion);
}
