package com.example.rechee.persona5calculator.services;

import android.support.annotation.Nullable;
import android.util.SparseArray;

import com.example.rechee.persona5calculator.models.Enumerations;
import com.example.rechee.persona5calculator.models.Enumerations.Arcana;
import com.example.rechee.persona5calculator.models.Persona;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import javax.inject.Named;

/**
 * Created by Rechee on 9/24/2017.
 */

public class PersonaFuser {

    private final Persona[] personasByLevel;
    private SparseArray<List<Persona>> personaByArcana;
    private HashMap<Arcana, HashMap<Arcana, Arcana>> arcanaTable;

    public PersonaFuser(Persona[] personasByLevel, HashMap<Arcana, HashMap<Arcana, Arcana>> arcanaTable){
        this.arcanaTable = arcanaTable;
        this.personasByLevel = personasByLevel;
        this.personaByArcana = this.personaByArcana();
    }

    private SparseArray<List<Persona>> personaByArcana(){
        SparseArray<List<Persona>> personaByArcana = new SparseArray<>(arcanaTable.size());
        for(Persona persona: personasByLevel){
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

        return personaByArcana;
    }

    @Nullable
    public Persona fuseNormal(Persona personaOne, Persona personaTwo) {
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
            //https://www.gamefaqs.com/ps2/932312-shin-megami-tensei-persona-3/faqs/49926

            for(int i = personaForResultArcana.size() - 1; i >= 0; i--){
                Persona persona = personaForResultArcana.get(i);

                if(persona.level < calculatedLevel){
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

            return null;
        }
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
