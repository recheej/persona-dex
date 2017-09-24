package com.example.rechee.persona5calculator.models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * Created by Rechee on 7/10/2017.
 */

public class PersonaGraph {

    private final HashMap<String, List<PersonaEdge>> personaTable;

    public PersonaGraph() {
        this.personaTable = new HashMap<>();
    }

    public void addEdge(Persona personaOne, Persona personaTwo, Persona result){

        if(!personaTable.containsKey(result.name)){
            personaTable.put(result.name, new ArrayList<PersonaEdge>());
        }

        PersonaEdge newEdge = new PersonaEdge();
        newEdge.start = personaOne.name;
        newEdge.end = result.name;
        newEdge.pairPersona = personaTwo.name;

        if(personaTable.containsKey(personaOne.name)){
            personaTable.get(personaOne.name).add(newEdge);
        }
        else{
            List<PersonaEdge> edges = new ArrayList<>();
            edges.add(newEdge);
            personaTable.put(personaOne.name, edges);
        }

        PersonaEdge edgeTwo = new PersonaEdge();
        edgeTwo.start = personaTwo.name;
        edgeTwo.end = result.name;
        edgeTwo.pairPersona = personaOne.name;

        if(personaTable.containsKey(personaTwo.name)){
            personaTable.get(personaTwo.name).add(edgeTwo);
        }
        else{
            List<PersonaEdge> edges = new ArrayList<>();
            edges.add(edgeTwo);
            personaTable.put(personaTwo.name, edges);
        }
    }

    public PersonaEdge[] edgesFrom(Persona persona){
        List<PersonaEdge> edgesFrom = this.personaTable.get(persona.name);

        if(edgesFrom == null){
            return new PersonaEdge[0];
        }

        return edgesFrom.toArray(new PersonaEdge[edgesFrom.size()]);
    }

    public PersonaEdge[] edgesTo(Persona persona){
        List<PersonaEdge> edgesTo = new ArrayList<>();

        for(String personaName: this.personaTable.keySet()){
            if(Objects.equals(personaName, persona.name)){
                continue;
            }

            for (PersonaEdge edgeForPersona: personaTable.get(personaName)){
                if(Objects.equals(edgeForPersona.end, persona.name)){
                    edgesTo.add(edgeForPersona);
                }
            }
        }

        return edgesTo.toArray(new PersonaEdge[edgesTo.size()]);
    }
}
