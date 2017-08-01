package com.example.rechee.persona5calculator.models;

import java.util.List;

/**
 * Created by Rechee on 7/22/2017.
 */

public class PersonaStore {
    PersonaEdge[] edgesFrom;
    PersonaEdge[] edgesTo;

    public PersonaStore(PersonaEdge[] edgesFrom, PersonaEdge[] edgesTo){
        this.edgesFrom = edgesFrom;
        this.edgesTo = edgesTo;
    }

    public PersonaEdge[] edgesTo() {
        return this.edgesTo;
    }

    public PersonaEdge[] edgesFrom() {
        return this.edgesFrom;
    }
}
