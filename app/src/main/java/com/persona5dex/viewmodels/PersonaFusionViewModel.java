package com.persona5dex.viewmodels;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;

import com.persona5dex.dagger.AndroidViewModels.AndroidViewModelRepositoryModule;
import com.persona5dex.dagger.Persona5ApplicationComponent;
import com.persona5dex.models.Pair;
import com.persona5dex.models.PersonaDetailSkill;
import com.persona5dex.models.PersonaEdgeDisplay;
import com.persona5dex.models.RawPersonaEdge;
import com.persona5dex.repositories.PersonaDisplayEdgesRepository;
import com.persona5dex.repositories.PersonaEdgesRepository;
import com.persona5dex.repositories.PersonaSkillsRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Rechee on 11/28/2017.
 */

public class PersonaFusionViewModel extends ViewModel {

    @Inject
    PersonaDisplayEdgesRepository repository;

    private LiveData<List<PersonaEdgeDisplay>> personaToEdges;
    private LiveData<List<PersonaEdgeDisplay>> personaFromEdges;
    private int personaID;
    private boolean isToList;
    private LiveData<String> personaName;

    public PersonaFusionViewModel(PersonaDisplayEdgesRepository repository){
        this.repository = repository;
    }

    public PersonaFusionViewModel() {}

    public void init(Persona5ApplicationComponent component, int personaID, boolean isToList) {
        component
                .plus(new AndroidViewModelRepositoryModule())
                .inject(this);

        this.personaID = personaID;
        this.isToList = isToList;
    }

    public static List<PersonaEdgeDisplay> filterOutDuplicateEdges(List<PersonaEdgeDisplay>  edges, int personaID, boolean isToList){
        HashSet<Pair<Integer, Integer>> personaSet = new HashSet<>(2000);

        List<PersonaEdgeDisplay> filteredEdges = new ArrayList<>(2000);
        for (PersonaEdgeDisplay edge : edges) {
            Pair<Integer, Integer> pair;
            if(isToList){
                pair = new Pair<>(edge.leftPersonaID, edge.rightPersonaID);
            }
            else{
                if(edge.leftPersonaID == personaID){
                    pair = new Pair<>(edge.rightPersonaID, edge.resultPersonaID);
                }
                else{
                    pair = new Pair<>(edge.leftPersonaID, edge.resultPersonaID);
                }
            }

            if(personaSet.contains(pair)){
                continue;
            }

            filteredEdges.add(edge);
            personaSet.add(pair);
        }

        return filteredEdges;
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

    public LiveData<List<PersonaEdgeDisplay>> getEdges(){
        if(isToList){
            if(personaToEdges == null){
                personaToEdges = repository.getEdgesToPersona(personaID);
            }
            return personaToEdges;
        }
        else{
            if(personaFromEdges == null){
                personaFromEdges = repository.getEdgesFromPersona(personaID);
            }

            return  Transformations.map(personaFromEdges, new Function<List<PersonaEdgeDisplay>, List<PersonaEdgeDisplay>>() {
                @Override
                public List<PersonaEdgeDisplay> apply(List<PersonaEdgeDisplay> input) {

                    input = filterOutDuplicateEdges(input, personaID, isToList);
                    for (PersonaEdgeDisplay edgeDisplay : input) {
                        //we want the left to be the persona that's not the current persona's

                        String left;
                        String right;

                        int leftPersonaID;
                        int rightPersonaID;

                        if (edgeDisplay.leftPersonaID == personaID) {
                            left = edgeDisplay.rightPersonaName;
                            leftPersonaID = edgeDisplay.rightPersonaID;
                        } else {
                            left = edgeDisplay.leftPersonaName;
                            leftPersonaID = edgeDisplay.leftPersonaID;
                        }
                        right = edgeDisplay.resultPersonaName;
                        rightPersonaID = edgeDisplay.rightPersonaID;

                        edgeDisplay.leftPersonaName = left;
                        edgeDisplay.rightPersonaName = right;
                        edgeDisplay.leftPersonaID = leftPersonaID;
                        edgeDisplay.rightPersonaID = rightPersonaID;
                    }

                    Collections.sort(input, new Comparator<PersonaEdgeDisplay>() {
                        @Override
                        public int compare(PersonaEdgeDisplay one, PersonaEdgeDisplay two) {
                            if(one.leftPersonaID == two.leftPersonaID){
                                return one.rightPersonaName.compareTo(two.rightPersonaName);
                            }

                            return one.leftPersonaName.compareTo(two.leftPersonaName);
                        }
                    });

                    return input;
                }
            });
        }
    }

    public LiveData<String> getPersonaName(int personaID){
        if(this.personaName == null){
            this.personaName = repository.getPersonaName(personaID);
        }

        return this.personaName;
    }
}
