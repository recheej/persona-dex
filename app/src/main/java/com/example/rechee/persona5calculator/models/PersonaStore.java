package com.example.rechee.persona5calculator.models;

/**
 * Created by Rechee on 7/22/2017.
 */

public class PersonaStore {
    private RawPersonaEdge[] edgesFrom;
    private RawPersonaEdge[] edgesTo;

    public PersonaStore(RawPersonaEdge[] edgesFrom, RawPersonaEdge[] edgesTo){
        this.edgesFrom = edgesFrom;
        this.edgesTo = edgesTo;
    }

    public void setEdgesFrom(RawPersonaEdge[] edgesFrom) {
        this.edgesFrom = edgesFrom;
    }

    public void setEdgesTo(RawPersonaEdge[] edgesTo) {
        this.edgesTo = edgesTo;
    }

    public RawPersonaEdge[] edgesTo() {
        return this.edgesTo;
    }

    public RawPersonaEdge[] edgesFrom() {
        return this.edgesFrom;
    }
}
