package com.persona5dex.repositories;

import com.persona5dex.models.Persona;

import java.util.Map;
import java.util.Set;

/**
 * Created by Rechee on 7/8/2017.
 * Used for transfering persona data between screens
 */

public interface PersonaTransferRepository {
    void storePersonaForDetail(Persona persona);
    Persona getDetailPersona();
    void storePersonaForFusion(Persona personaForFusion);
    int getPersonaForFusion();
    String getPersonaName(int personaID);
    Map<String, Integer> getDLCPersonaForSettings();
    Set<String> getOwnedDlCPersonaIDs();
    boolean rarePersonaAllowedInFusions();
}
