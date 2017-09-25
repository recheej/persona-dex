package com.example.rechee.persona5calculator.viewmodels;

import android.arch.lifecycle.ViewModel;

import com.example.rechee.persona5calculator.models.Pair;
import com.example.rechee.persona5calculator.models.Persona;
import com.example.rechee.persona5calculator.models.PersonaEdge;
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

    public PersonaStore getEdgesForPersona(Persona persona) {
        return this.getEdgesForPersona(persona.name);
    }

    public PersonaStore getEdgesForPersona(String personaName) {
        PersonaStore personaStore = personaEdgeRepository.getEdgesForPersona(personaName);

        personaStore.setEdgesFrom(filterOutDuplicateEdges(personaStore.edgesFrom(), personaName, false));
        personaStore.setEdgesTo(filterOutDuplicateEdges(personaStore.edgesTo(), personaName, true));

        return personaStore;
    }

    public static PersonaEdge[] filterOutDuplicateEdges(PersonaEdge[] edges, String personaName, boolean isToList){
        HashSet<Pair<String, String>> personaSet = new HashSet<>(2000);

        List<PersonaEdge> filteredEdges = new ArrayList<>(2000);
        for (PersonaEdge edge : edges) {
            Pair<String, String> pair;
            if(isToList){
                pair = new Pair<>(edge.start, edge.pairPersona);
            }
            else{
                if(edge.start.equals(personaName)){
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

        return filteredEdges.toArray(new PersonaEdge[filteredEdges.size()]);
    }

    public void storePersonaForDetail(String personaName){
        personaListViewModel.storePersonaForDetail(personaName);
    }
}
