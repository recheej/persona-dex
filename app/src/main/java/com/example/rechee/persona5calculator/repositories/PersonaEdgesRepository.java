package com.example.rechee.persona5calculator.repositories;

import com.example.rechee.persona5calculator.models.Persona;
import com.example.rechee.persona5calculator.models.PersonaStore;

/**
 * Created by Rechee on 7/22/2017.
 */

public interface PersonaEdgesRepository {
    void addPersonaEdges(Persona persona, PersonaStore personaStore);
    void markFinished();
    void markInit();
    PersonaStore getEdgesForPersona(int personaID);
}
