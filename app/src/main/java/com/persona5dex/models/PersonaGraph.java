package com.persona5dex.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Rechee on 7/10/2017.
 */

public class PersonaGraph {

    private final HashMap<Integer, List<RawPersonaEdge>> personaTable;

    public PersonaGraph() {
        this.personaTable = new HashMap<>();
    }

    public void addEdge(PersonaForFusionService personaOne, PersonaForFusionService personaTwo, PersonaForFusionService result){

        if(!personaTable.containsKey(result.getId())){
            personaTable.put(result.getId(), new ArrayList<RawPersonaEdge>());
        }

        RawPersonaEdge newEdge = new RawPersonaEdge();
        newEdge.start = personaOne.getId();
        newEdge.end = result.getId();
        newEdge.pairPersona = personaTwo.getId();

        if(personaTable.containsKey(personaOne.getId())){
            personaTable.get(personaOne.getId()).add(newEdge);
        }
        else{
            List<RawPersonaEdge> edges = new ArrayList<>();
            edges.add(newEdge);
            personaTable.put(personaOne.getId(), edges);
        }

        RawPersonaEdge edgeTwo = new RawPersonaEdge();
        edgeTwo.start = personaTwo.getId();
        edgeTwo.end = result.getId();
        edgeTwo.pairPersona = personaOne.getId();

        if(personaTable.containsKey(personaTwo.getId())){
            personaTable.get(personaTwo.getId()).add(edgeTwo);
        }
        else{
            List<RawPersonaEdge> edges = new ArrayList<>();
            edges.add(edgeTwo);
            personaTable.put(personaTwo.getId(), edges);
        }
    }

    public RawPersonaEdge[] edgesFrom(PersonaForFusionService persona){
        List<RawPersonaEdge> edgesFrom = this.personaTable.get(persona.getId());

        if(edgesFrom == null){
            return new RawPersonaEdge[0];
        }

        return edgesFrom.toArray(new RawPersonaEdge[edgesFrom.size()]);
    }

    public RawPersonaEdge[] edgesTo(PersonaForFusionService persona){
        List<RawPersonaEdge> edgesTo = new ArrayList<>();

        for(int personaID: this.personaTable.keySet()){
            if(personaID == persona.getId()){
                continue;
            }

            for (RawPersonaEdge edgeForPersona: personaTable.get(personaID)){
                if(edgeForPersona.end == persona.getId()){
                    edgesTo.add(edgeForPersona);
                }
            }
        }

        return edgesTo.toArray(new RawPersonaEdge[edgesTo.size()]);
    }
}
