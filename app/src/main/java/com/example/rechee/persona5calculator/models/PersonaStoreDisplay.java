package com.example.rechee.persona5calculator.models;

/**
 * Created by Rechee on 10/1/2017.
 */

public class PersonaStoreDisplay {
    private PersonaStoreDisplay[] edgesFrom;
    private PersonaStoreDisplay[] edgesTo;

    public void setEdgesFrom(PersonaStoreDisplay[] edgesFrom) {
        this.edgesFrom = edgesFrom;
    }

    public void setEdgesTo(PersonaStoreDisplay[] edgesTo) {
        this.edgesTo = edgesTo;
    }

    public PersonaStoreDisplay[] edgesTo() {
        return this.edgesTo;
    }

    public PersonaStoreDisplay[] edgesFrom() {
        return this.edgesFrom;
    }
}
