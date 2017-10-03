package com.example.rechee.persona5calculator.viewmodels;

import android.arch.lifecycle.ViewModel;

import com.example.rechee.persona5calculator.models.Pair;
import com.example.rechee.persona5calculator.models.PersonaStoreDisplay;
import com.example.rechee.persona5calculator.models.RawPersonaEdge;
import com.example.rechee.persona5calculator.models.PersonaStore;
import com.example.rechee.persona5calculator.repositories.PersonaEdgesRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by Rechee on 7/30/2017.
 */

public class PersonaFusionListViewModel extends ViewModel {

    private final PersonaEdgesRepository personaEdgeRepository;
    private final PersonaListViewModel personaListViewModel;

    public PersonaFusionListViewModel(PersonaEdgesRepository personaEdgeRepository, PersonaListViewModel personaListViewModel){
        this.personaEdgeRepository = personaEdgeRepository;
        this.personaListViewModel = personaListViewModel;
    }

    public PersonaStoreDisplay getEdgesForPersona(int personaID) {
        PersonaStoreDisplay personaStore = personaEdgeRepository.getEdgesForPersona(personaID);

        personaStore.setEdgesFrom(filterOutDuplicateEdges(personaStore.edgesFrom(), personaID, false));
        personaStore.setEdgesTo(filterOutDuplicateEdges(personaStore.edgesTo(), personaID, true));

        return personaStore;
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
        personaListViewModel.storePersonaForDetail(personaName);
    }
}
