package com.example.rechee.persona5calculator;

import android.app.Activity;
import android.app.Application;
import android.support.annotation.Nullable;
import android.util.SparseArray;

import com.example.rechee.persona5calculator.dagger.ApplicationContextModule;
import com.example.rechee.persona5calculator.dagger.DaggerPersona5ApplicationComponent;
import com.example.rechee.persona5calculator.dagger.Persona5ApplicationComponent;
import com.example.rechee.persona5calculator.dagger.PersonaFileModule;
import com.example.rechee.persona5calculator.models.PersonaEdge;
import com.example.rechee.persona5calculator.models.PersonaGraph;
import com.example.rechee.persona5calculator.models.Persona;
import com.example.rechee.persona5calculator.models.RawArcanaMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

import static com.example.rechee.persona5calculator.models.Enumerations.*;

/**
 * Created by Rechee on 7/1/2017.
 */

public class Persona5Application extends Application {
    private Persona5ApplicationComponent component;

    public static Persona5Application get(Activity activity){
        return (Persona5Application) activity.getApplication();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        this.component = DaggerPersona5ApplicationComponent.builder()
                .personaFileModule(new PersonaFileModule(this))
                .applicationContextModule(new ApplicationContextModule(this))
                .build();

        RawArcanaMap[] arcanaMaps = this.component.arcanaMaps();
        PersonaUtilities personaUtilities = PersonaUtilities.getUtilities();

        HashMap<Arcana, HashMap<Arcana, Arcana>> arcanaTable = new HashMap<>();

        for (RawArcanaMap arcanaMap: arcanaMaps){
            Arcana arcanaPersonaOne = personaUtilities.nameToArcana(arcanaMap.source[0]);
            Arcana arcanaPersonaTwo = personaUtilities.nameToArcana(arcanaMap.source[1]);

            Arcana resultArcana = personaUtilities.nameToArcana(arcanaMap.result);

            if(arcanaTable.containsKey(arcanaPersonaOne)){
                arcanaTable.get(arcanaPersonaOne).put(arcanaPersonaTwo, resultArcana);
            }
            else{
                HashMap<Arcana, Arcana> innerTable = new HashMap<>();
                innerTable.put(arcanaPersonaTwo, resultArcana);
                arcanaTable.put(arcanaPersonaOne, innerTable);
            }
        }

        Persona[] personas = getAllPersonas();
        Arrays.sort(personas, new Comparator<Persona>() {
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

        SparseArray<List<Persona>> personaByArcana = new SparseArray<>(arcanaTable.size());
        for(Persona persona: personas){
            int arcanaIndex = persona.getArcana().ordinal();
            List<Persona> personaList = personaByArcana.get(arcanaIndex);

            if(personaList == null){
                personaList = new ArrayList<>();
                personaList.add(persona);
                personaByArcana.put(persona.getArcana().ordinal(), personaList);
            }
            else{
                personaList.add(persona);
            }
        }

        Persona personOne = this.personaThatContainsName(personas, "high pixie");
        Persona personaTwo = this.personaThatContainsName( personas, "Obariyon");

        Persona result = this.fuseNormal(personOne, personaTwo, personaByArcana, arcanaTable);
        this.makePersonaGraph(personas, personaByArcana, arcanaTable);
    }

    private void makePersonaGraph(Persona[] personas, SparseArray<List<Persona>> personaByArcana, HashMap<Arcana, HashMap<Arcana, Arcana>> arcanaTable){

        HashMap<String, HashSet<String>> pairSet = new HashMap<>(210);
        PersonaGraph graph = new PersonaGraph();

        Persona obariyonPersona = this.personaThatContainsName(personas, "obari");

        for (Persona personaOne: personas){

            if(personaOne.rare || personaOne.special){
                continue;
            }

            for (Persona personaTwo: personas){
                if(personaTwo.rare || personaTwo.special){
                    continue;
                }

                if(pairSet.containsKey(personaOne.name)){
                    if(pairSet.get(personaOne.name).contains(personaTwo.name)){
                        continue;
                    }
                }
                else if(pairSet.containsKey(personaTwo.name)){
                    if(pairSet.get(personaTwo.name).contains(personaOne.name)){
                        continue;
                    }
                }

                Persona result = this.fuseNormal(personaOne, personaTwo, personaByArcana, arcanaTable);

                if(result != null) {
                    graph.addLink(personaOne, personaTwo, result);
                }

                if(pairSet.containsKey(personaOne.name)){
                    pairSet.get(personaOne.name).add(personaTwo.name);
                }
                else{
                    HashSet<String> set = new HashSet<>();
                    set.add(personaTwo.name);

                    pairSet.put(personaOne.name, set);
                }

                if(pairSet.containsKey(personaTwo.name)){
                    pairSet.get(personaTwo.name).add(personaOne.name);
                }
                else{
                    HashSet<String> set = new HashSet<>();
                    set.add(personaOne.name);

                    pairSet.put(personaTwo.name, set);
                }
            }
        }


        List<PersonaEdge> edgesFrom = graph.edgesFrom(this.personaThatContainsName(personas, "arsene"));
        List<PersonaEdge> edgesTo = graph.edgesTo(this.personaThatContainsName(personas, "arsene"));


        String test = "";
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

    @Nullable
    private Persona fuseNormal(Persona personaOne, Persona personaTwo, SparseArray<List<Persona>> personaByArcana, HashMap<Arcana, HashMap<Arcana, Arcana>> arcanaTable) {
        if(personaOne == personaTwo){
            return null;
        }

        if(!this.personaIsValidInFusion(personaOne) || !this.personaIsValidInFusion(personaTwo)){
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

        float calculatedLevel = (personaOne.level + personaTwo.level) / 2;

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

                if(this.personaIsValidInFusion(fusedPersona) && !(fusedPersona.name.equals(personaOne.name) || fusedPersona.name.equals(personaTwo.name))){
                    return fusedPersona;
                }
            }

            return null;
        }
    }

    private boolean personaIsValidInFusion(Persona persona){
        return !(persona.rare || persona.special || persona.dlc);
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

    public Persona[] getAllPersonas() {
        Persona[] personas = this.component.allPersonas();
        return personas;
    }

    public Persona5ApplicationComponent getComponent() {
        return this.component;
    }
}
