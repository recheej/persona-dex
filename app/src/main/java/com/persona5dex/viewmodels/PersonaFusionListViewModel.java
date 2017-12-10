package com.persona5dex.viewmodels;

import com.persona5dex.models.Pair;
import com.persona5dex.models.PersonaEdgeDisplay;
import com.persona5dex.models.PersonaStoreDisplay;
import com.persona5dex.models.RawPersonaEdge;
import com.persona5dex.models.PersonaStore;
import com.persona5dex.repositories.PersonaEdgesRepository;
import com.persona5dex.repositories.PersonaTransferRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by Rechee on 7/30/2017.
 */

public class PersonaFusionListViewModel{

    private final PersonaEdgesRepository personaEdgeRepository;
    private final PersonaTransferRepository transferRepository;

    public PersonaFusionListViewModel(PersonaEdgesRepository personaEdgeRepository, PersonaTransferRepository transferRepository){
        this.personaEdgeRepository = personaEdgeRepository;
        this.transferRepository = transferRepository;
    }

    public PersonaStoreDisplay getEdgesForPersona(int personaID) {
        PersonaStore personaStore = personaEdgeRepository.getEdgesForPersona(personaID);

        personaStore.setEdgesFrom(filterOutDuplicateEdges(personaStore.edgesFrom(), personaID, false));
        personaStore.setEdgesTo(filterOutDuplicateEdges(personaStore.edgesTo(), personaID, true));

        return this.mapRawStoreToDisplay(personaStore, personaID);
    }

    /**
     * Helper method to map raw persona edges to ones for display
     * @param store Persona store which has edges
     * @param personaID ID of persona
     * @return Persona Store Display. Which basically maps
     */
    private PersonaStoreDisplay mapRawStoreToDisplay(PersonaStore store, int personaID){
        PersonaStoreDisplay personaStoreDisplay = new PersonaStoreDisplay();

        RawPersonaEdge[] rawEdgesFrom = store.edgesFrom();
        RawPersonaEdge[] rawEdgesTo = store.edgesTo();

        PersonaEdgeDisplay[] edgesFrom = new PersonaEdgeDisplay[rawEdgesFrom.length];
        PersonaEdgeDisplay[] edgesTo = new PersonaEdgeDisplay[rawEdgesTo.length];

        for (int i = 0; i < rawEdgesFrom.length; i++) {
            RawPersonaEdge rawPersonaEdge = rawEdgesFrom[i];

            PersonaEdgeDisplay edgeDisplay = new PersonaEdgeDisplay();

            String left;
            String right;

            if (rawPersonaEdge.start == personaID) {
                left = transferRepository.getPersonaName(rawPersonaEdge.pairPersona);
            } else {
                left = transferRepository.getPersonaName(rawPersonaEdge.start);
            }
            right = transferRepository.getPersonaName(rawPersonaEdge.end);

            edgeDisplay.leftPersonaName = left;
            edgeDisplay.rightPersonaName = right;

            edgesFrom[i] = edgeDisplay;
        }

        for (int i = 0; i < rawEdgesTo.length; i++) {
            RawPersonaEdge rawPersonaEdge = rawEdgesTo[i];

            PersonaEdgeDisplay edgeDisplay = new PersonaEdgeDisplay();

            edgeDisplay.leftPersonaName = transferRepository.getPersonaName(rawPersonaEdge.start);
            edgeDisplay.rightPersonaName = transferRepository.getPersonaName(rawPersonaEdge.pairPersona);

            edgesTo[i] = edgeDisplay;
        }

        personaStoreDisplay.setEdgesFrom(edgesFrom);
        personaStoreDisplay.setEdgesTo(edgesTo);

        return personaStoreDisplay;
    }

    public static RawPersonaEdge[] filterOutDuplicateEdges(RawPersonaEdge[] edges, int personaID, boolean isToList){
        HashSet<Pair<Integer, Integer>> personaSet = new HashSet<>(2000);

        List<RawPersonaEdge> filteredEdges = new ArrayList<>(2000);
        for (RawPersonaEdge edge : edges) {
            Pair<Integer, Integer> pair;
            if(isToList){
                pair = new Pair<>(edge.start, edge.pairPersona);
            }
            else{
                if(edge.start == personaID){
                    pair = new Pair<>(edge.pairPersona, edge.end);
                }
                else{
                    pair = new Pair<>(edge.start, edge.end);
                }
            }

            if(personaSet.contains(pair)){
                continue;
            }

            filteredEdges.add(edge);
            personaSet.add(pair);
        }

        return filteredEdges.toArray(new RawPersonaEdge[filteredEdges.size()]);
    }

    public void storePersonaForDetail(String personaName){
        //personaListViewModel.storePersonaForDetail(personaName);
    }
}
