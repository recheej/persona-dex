package com.example.rechee.persona5calculator.repositories;

import com.example.rechee.persona5calculator.models.Persona;

/**
 * Created by Rechee on 7/8/2017.
 * Used for transfering persona data between screens
 */

public interface PersonaTransferRepository {
    void storePersonaForDetail(Persona persona);
    Persona getDetailPersona();
    void storePersonaForFusion(Persona personaForFusion);
    String getPersonaForFusion();
}
