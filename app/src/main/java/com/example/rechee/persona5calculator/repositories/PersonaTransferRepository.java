package com.example.rechee.persona5calculator.repositories;

import com.example.rechee.persona5calculator.models.Persona;

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
    void commit();
    void setPersonaIDs(Persona[] personas);
    Map<String, Integer> getDLCPersonaForSettings();
    Set<String> getOwnedDlCPersonaIDs();
    boolean rarePersonaAllowedInFusions();
}
