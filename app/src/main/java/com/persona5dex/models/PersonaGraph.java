package com.persona5dex.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * Created by Rechee on 7/10/2017.
 */

public class PersonaGraph {

    private final HashMap<Integer, List<RawPersonaEdge>> personaTable;

    public PersonaGraph() {
        this.personaTable = new HashMap<>();
    }

    public void addEdge(Persona personaOne, Persona personaTwo, Persona result){

        if(!personaTable.containsKey(result.id)){
            personaTable.put(result.id, new ArrayList<RawPersonaEdge>());
        }

        RawPersonaEdge newEdge = new RawPersonaEdge();
        newEdge.start = personaOne.id;
        newEdge.end = result.id;
        newEdge.pairPersona = personaTwo.id;

        if(personaTable.containsKey(personaOne.id)){
            personaTable.get(personaOne.id).add(newEdge);
        }
        else{
            List<RawPersonaEdge> edges = new ArrayList<>();
            edges.add(newEdge);
            personaTable.put(personaOne.id, edges);
        }

        RawPersonaEdge edgeTwo = new RawPersonaEdge();
        edgeTwo.start = personaTwo.id;
        edgeTwo.end = result.id;
        edgeTwo.pairPersona = personaOne.id;

        if(personaTable.containsKey(personaTwo.id)){
            personaTable.get(personaTwo.id).add(edgeTwo);
        }
        else{
            List<RawPersonaEdge> edges = new ArrayList<>();
            edges.add(edgeTwo);
            personaTable.put(personaTwo.id, edges);
        }
    }

    public RawPersonaEdge[] edgesFrom(Persona persona){
        List<RawPersonaEdge> edgesFrom = this.personaTable.get(persona.id);

        if(edgesFrom == null){
            return new RawPersonaEdge[0];
        }

        return edgesFrom.toArray(new RawPersonaEdge[edgesFrom.size()]);
    }

    public RawPersonaEdge[] edgesTo(Persona persona){
        List<RawPersonaEdge> edgesTo = new ArrayList<>();

        for(int personaID: this.personaTable.keySet()){
            if(personaID == persona.id ){
                continue;
            }

            for (RawPersonaEdge edgeForPersona: personaTable.get(personaID)){
                if(edgeForPersona.end == persona.id){
                    edgesTo.add(edgeForPersona);
                }
            }
        }

        return edgesTo.toArray(new RawPersonaEdge[edgesTo.size()]);
    }
}
