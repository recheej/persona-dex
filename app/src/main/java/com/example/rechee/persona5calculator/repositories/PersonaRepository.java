package com.example.rechee.persona5calculator.repositories;

import com.example.rechee.persona5calculator.models.Persona;
import com.example.rechee.persona5calculator.models.RawArcanaMap;

/**
 * Created by Rechee on 7/8/2017.
 */

public interface PersonaRepository {
    Persona[] allPersonas();
    RawArcanaMap[] rawArcanas();
}
