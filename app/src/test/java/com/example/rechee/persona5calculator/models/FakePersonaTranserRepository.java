package com.example.rechee.persona5calculator.models;

import com.example.rechee.persona5calculator.repositories.PersonaTransferRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Rechee on 10/7/2017.
 */

public class FakePersonaTranserRepository implements PersonaTransferRepository {
    @Override
    public void storePersonaForDetail(Persona persona) {

    }

    @Override
    public Persona getDetailPersona() {
        return null;
    }

    @Override
    public void storePersonaForFusion(Persona personaForFusion) {

    }

    @Override
    public int getPersonaForFusion() {
        return 0;
    }

    @Override
    public String getPersonaName(int personaID) {
        return null;
    }

    @Override
    public void commit() {

    }

    @Override
    public void setPersonaIDs(Persona[] personas) {

    }

    @Override
    public Map<String, Integer> getDLCPersonaForSettings() {
        Map<String, Integer> output = new HashMap<>(10);
        output.put("PersonaTwo", 1);
        output.put("PersonaOne", 0);
        output.put("PersonaThree", 2);

        return output;
    }

    @Override
    public Set<String> getOwnedDlCPersonaIDs() {
        return null;
    }

    @Override
    public boolean rarePersonaAllowedInFusions() {
        return false;
    }
}
