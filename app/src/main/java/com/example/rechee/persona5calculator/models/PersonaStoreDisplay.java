package com.example.rechee.persona5calculator.models;

/**
 * Created by Rechee on 10/1/2017.
 */

public class PersonaStoreDisplay {
    private PersonaEdgeDisplay[] edgesFrom;
    private PersonaEdgeDisplay[] edgesTo;

    public void setEdgesFrom(PersonaEdgeDisplay[] edgesFrom) {
        this.edgesFrom = edgesFrom;
    }

    public void setEdgesTo(PersonaEdgeDisplay[] edgesTo) {
        this.edgesTo = edgesTo;
    }

    public PersonaEdgeDisplay[] edgesTo() {
        return this.edgesTo;
    }

    public PersonaEdgeDisplay[] edgesFrom() {
        return this.edgesFrom;
    }
}
