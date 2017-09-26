package com.example.rechee.persona5calculator.services;

import android.support.annotation.Nullable;
import android.util.SparseArray;

import com.example.rechee.persona5calculator.models.Enumerations.Arcana;
import com.example.rechee.persona5calculator.models.Persona;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by Rechee on 9/24/2017.
 */

public class PersonaFuser {

    private final Persona[] personasByLevel;
    private SparseArray<List<Persona>> personaByArcana;
    private HashMap<Arcana, HashMap<Arcana, Arcana>> arcanaTable;
    private Map<String, int[]> rareComboMap;

    private final String[] rarePersonas = {"Regent", "Queen's Necklace", "Stone of Scone",
            "Koh-i-Noor", "Orlov", "Emperor's Amulet", "Hope Diamond", "Crystal Skull"};

    public PersonaFuser(Persona[] personasByLevel, HashMap<Arcana, HashMap<Arcana, Arcana>> arcanaTable, Map<String, int[]> rareComboMap){
        this.arcanaTable = arcanaTable;
        this.personasByLevel = personasByLevel;
        this.rareComboMap = rareComboMap;
        this.personaByArcana = this.personaByArcana();
    }

    private int getRarePersonaIndex(String personaName){
        //get the index of the index of the rare persona's name within the rare persona array

        for (int i = 0; i < rarePersonas.length; i++) {
            String rarePersona = rarePersonas[i];
            if (rarePersona.equals(personaName)) {
                return i;
            }
        }

        return 0;
    }

    @Nullable
    private Persona fuseRare(Persona normalPersona, Persona rarePersona){
        int rarePersonaIndex = this.getRarePersonaIndex(rarePersona.name);
        int modifier = this.rareComboMap.get(normalPersona.arcanaName)[rarePersonaIndex];

        List<Persona> personasOfSameArcana = personaByArcana.get(normalPersona.getArcana().ordinal());

        int personaIndex = 0;
        final int arcanaSize = personasOfSameArcana.size();

        for (int i = 0; i < arcanaSize; i++) {
            Persona otherPersona = personasOfSameArcana.get(i);

            if(otherPersona.name.equals(normalPersona.name)){
                personaIndex = i;
                break;
            }
        }

        int newPersonaIndex = personaIndex + modifier;
        if(newPersonaIndex >= 0 && newPersonaIndex < arcanaSize){
            Persona result = personasOfSameArcana.get(newPersonaIndex);

            if(this.personaIsValidInFusionResult(result)){
                return result;
            }

            //if the result isn't valid, loop through until we get a valid one
            while(!(newPersonaIndex >= 0 && newPersonaIndex < arcanaSize)){
                if(modifier > 0){
                    modifier++;
                }
                else if(modifier < 0){
                    modifier--;
                }

                newPersonaIndex = personaIndex + modifier;

                if(newPersonaIndex >= 0 && newPersonaIndex < arcanaSize){

                    result = personasOfSameArcana.get(newPersonaIndex);

                    if(this.personaIsValidInFusionResult(result)){
                        return result;
                    }
                }
            }

            return null;
        }

        return null;
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

        if(!personaIsValidInRecipe(personaOne) || !personaIsValidInRecipe(personaTwo)){
            return null;
        }

        if(personaOne.rare && personaTwo.rare){
            return null;
        }

        if(personaOne.rare){
            return fuseRare(personaTwo, personaOne);
        }

        if(personaTwo.rare){
            return fuseRare(personaOne, personaTwo);
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

        int calculatedLevel = (personaOne.level + personaTwo.level) / 2;
        calculatedLevel += 1;

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
                    if(!this.personaIsValidInFusionResult(persona)|| Objects.equals(persona.name, personaOne.name) || Objects.equals(persona.name, personaTwo.name)){
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
                    if(!this.personaIsValidInFusionResult(persona) || persona.name.equals(personaOne.name) || persona.name.equals(personaTwo.name)){
                        continue;
                    }

                    return persona;
                }
            }

            return null;
        }
    }

    private boolean personaIsValidInRecipe(Persona persona){
        return true;
    }

    private boolean personaIsValidInFusionResult(Persona persona){
        return !persona.rare && !persona.special;
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
