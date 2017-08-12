package com.example.rechee.persona5calculator.repositories;

import com.example.rechee.persona5calculator.models.Persona;
import com.example.rechee.persona5calculator.models.PersonaEdge;
import com.example.rechee.persona5calculator.models.PersonaStore;

/**
 * Created by Rechee on 7/22/2017.
 */

public interface PersonaEdgesRepository {
    void addPersonaEdges(Persona persona, PersonaStore personaStore);
    void markFinished();
    void markInit();
    PersonaStore getEdgesForPersona(Persona persona);

    PersonaStore getEdgesForPersona(String personaName);
}
