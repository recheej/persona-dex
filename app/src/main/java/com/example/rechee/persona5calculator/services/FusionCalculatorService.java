package com.example.rechee.persona5calculator.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.SparseArray;

import com.example.rechee.persona5calculator.Persona5Application;
import com.example.rechee.persona5calculator.PersonaUtilities;
import com.example.rechee.persona5calculator.dagger.DaggerFusionCalculatorServiceComponent;
import com.example.rechee.persona5calculator.dagger.FusionCalculatorServiceComponent;
import com.example.rechee.persona5calculator.dagger.FusionServiceContextModule;
import com.example.rechee.persona5calculator.models.Enumerations;
import com.example.rechee.persona5calculator.models.Persona;
import com.example.rechee.persona5calculator.models.PersonaEdge;
import com.example.rechee.persona5calculator.models.PersonaGraph;
import com.example.rechee.persona5calculator.models.PersonaStore;
import com.example.rechee.persona5calculator.models.RawArcanaMap;
import com.example.rechee.persona5calculator.repositories.PersonaEdgesRepository;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Named;

import static com.example.rechee.persona5calculator.models.Enumerations.*;

/**
 * Created by Rechee on 7/17/2017.
 */

public class FusionCalculatorService extends IntentService {

    @Inject
    PersonaEdgesRepository personaEdgeRepository;
    @Inject
    SparseArray<List<Persona>> personaByArcana;
    @Inject
    @Named("personaByLevel") Persona[] personas;

    @Inject
    HashMap<Arcana, HashMap<Arcana, Arcana>> arcanaTable;

    private final static String SERVICE_NAME = "FusionCalculatorService";

    public FusionCalculatorService(){super(SERVICE_NAME);}

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        FusionCalculatorServiceComponent component = DaggerFusionCalculatorServiceComponent.builder()
                .persona5ApplicationComponent(Persona5Application.get(this).getComponent())
                .fusionServiceContextModule(new FusionServiceContextModule(this))
                .build();
        component.inject(this);

        Persona[] personsSortedByLevel = new Persona[personas.length];

        System.arraycopy(personas, 0, personsSortedByLevel, 0, personas.length);
        Arrays.sort(personsSortedByLevel, new Comparator<Persona>() {
            @Override
            public int compare(Persona o1, Persona o2) {
                if(o1.level < o2.level){
                    return -1;
                }

                if(o1.level == o2.level){
                    return 0;
                }

                return 1;
            }
        });

        PersonaGraph graph = this.makePersonaGraph(personsSortedByLevel, personaByArcana, arcanaTable);

        for(Persona persona: personsSortedByLevel){
            PersonaEdge[] edgesTo = graph.edgesTo(persona);
            PersonaEdge[] edgesFrom = graph.edgesFrom(persona);

            PersonaStore store = new PersonaStore(edgesFrom, edgesTo);
            this.personaEdgeRepository.addPersonaEdges(persona, store);
        }

        this.personaEdgeRepository.markFinished();
        stopSelf();
    }

    private PersonaGraph makePersonaGraph(Persona[] personas, SparseArray<List<Persona>> personaByArcana, HashMap<Arcana, HashMap<Arcana, Arcana>> arcanaTable){

        HashSet<Integer> pairSet = new HashSet<>(210);
        PersonaGraph graph = new PersonaGraph();

        for (Persona personaOne: personas){
            for (Persona personaTwo: personas){

                //use the xor of the name hash code's to avoid duplicates
                int pairHashCode = personaOne.name.hashCode() ^ personaTwo.name.hashCode();
                if(pairSet.contains(pairHashCode)){
                    continue;
                }

                Persona result = this.fuseNormal(personaOne, personaTwo, personaByArcana, arcanaTable);

                if(result != null) {
                    graph.addLink(personaOne, personaTwo, result);
                }

                pairSet.add(pairHashCode);
            }
        }

        return graph;
    }

    @Nullable
    private Persona fuseNormal(Persona personaOne, Persona personaTwo, SparseArray<List<Persona>> personaByArcana, HashMap<Arcana, HashMap<Arcana, Arcana>> arcanaTable) {
        if(personaOne == personaTwo){
            return null;
        }

        if(!this.personaIsValidInFusion(personaOne) || !this.personaIsValidInFusion(personaTwo)){
            return null;
        }

        if(personaOne.max || personaTwo.max){
            return null;
        }

        Arcana resultArcana;
        if(personaOne.getArcana() == personaTwo.getArcana()){
            resultArcana = personaOne.getArcana();
        }
        else{
            resultArcana = getResultArcana(personaOne.getArcana(), personaTwo.getArcana(), arcanaTable);
        }

        if(resultArcana == null){
            return null;
        }

        int calculatedLevel = ((personaOne.level + personaTwo.level) / 2) + 1;

        List<Persona> personaForResultArcana = personaByArcana.get(resultArcana.ordinal());

        if(personaForResultArcana.size() == 0){
            //this should never happen, but hey you never know
            return null;
        }

        if(personaOne.getArcana() == personaTwo.getArcana()){
            //fusion theory according to this: http://persona4.wikidot.com/fusiontutor
            //https://github.com/chinhodado/persona5_calculator/blob/master/src/FusionCalculator.js

            for(int i = personaForResultArcana.size() - 1; i >= 0; i--){
                Persona persona = personaForResultArcana.get(i);

                if(persona.level <= calculatedLevel){
                    if(!this.personaIsValidInFusion(persona)|| Objects.equals(persona.name, personaOne.name) || Objects.equals(persona.name, personaTwo.name)){
                        continue;
                    }

                    return persona;
                }
            }

            return null;
        }
        else{
            for (Persona persona : personaForResultArcana) {
                if (persona.level >= calculatedLevel) {
                    if(!this.personaIsValidInFusion(persona) || persona.name.equals(personaOne.name) || persona.name.equals(personaTwo.name)){
                        continue;
                    }

                    return persona;
                }
            }

            if(personaByArcana.size() == 0){
                return null;
            }

            for(int i = personaForResultArcana.size() - 1; i >= 0; i--){
                Persona fusedPersona = personaForResultArcana.get(i);

                if(fusedPersona.name.equals(personaOne.name) || fusedPersona.name.equals(personaTwo.name)){
                    continue;
                }

                if(fusedPersona.level < calculatedLevel && this.personaIsValidInFusion(fusedPersona)){
                    return fusedPersona;
                }
            }

            return null;
        }
    }

    @Nullable
    private Persona personaThatContainsName(Persona[] personas, String personaName){
        for(Persona persona: personas){
            if(persona.name.toLowerCase().contains(personaName.toLowerCase())){
                return persona;
            }
        }

        return null;
    }

    private boolean personaIsValidInFusion(Persona persona){
        return !persona.rare && !persona.special && !persona.dlc;
    }

    @Nullable
    private Arcana getResultArcana(Arcana arcanaOne, Arcana arcanaTwo, HashMap<Arcana, HashMap<Arcana, Arcana>> arcanaTable){
        if(arcanaTable.containsKey(arcanaOne)){
            HashMap<Arcana, Arcana> innerTable = arcanaTable.get(arcanaOne);
            if(innerTable.containsKey(arcanaTwo)){
                return innerTable.get(arcanaTwo);
            }
        }

        return null;
    }
}
