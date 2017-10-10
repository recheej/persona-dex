package com.persona5dex.services;

import android.support.annotation.Nullable;
import android.util.SparseArray;

import com.persona5dex.models.Enumerations.Arcana;
import com.persona5dex.models.Persona;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Created by Rechee on 9/24/2017.
 */

public class PersonaFuser {

    private final Persona[] personasByLevel;
    private final boolean rarePersonaAllowedInFusion;
    private final Set<Integer> ownedDLCPersonaIDs;
    private SparseArray<List<Persona>> personaByArcana;
    private HashMap<Arcana, HashMap<Arcana, Arcana>> arcanaTable;
    private Map<String, int[]> rareComboMap;

    private final String[] rarePersonas = {"Regent", "Queen's Necklace", "Stone of Scone",
            "Koh-i-Noor", "Orlov", "Emperor's Amulet", "Hope Diamond", "Crystal Skull"};

    public PersonaFuser(PersonaFusionArgs args){
        this.arcanaTable = args.arcanaTable;
        this.personasByLevel = args.personasByLevel;
        this.rareComboMap = args.rareComboMap;
        this.rarePersonaAllowedInFusion = args.rarePersonaAllowedInFusion;
        this.ownedDLCPersonaIDs = convertIDsToIntegers(args.ownedDLCPersonaIDs);
        this.personaByArcana = this.personaByArcana();
    }

    private Set<Integer> convertIDsToIntegers(Set<String> ids){
        final int setSize = ids.size();
        Set<Integer> integerSet = new HashSet<>(setSize);
        for (String s : ids.toArray(new String[setSize])) {
            integerSet.add(Integer.parseInt(s));
        }

        return integerSet;
    }

    public static class PersonaFusionArgs {
        public Persona[] personasByLevel;
        public HashMap<Arcana, HashMap<Arcana, Arcana>> arcanaTable;
        public Map<String, int[]> rareComboMap;
        public boolean rarePersonaAllowedInFusion;
        public Set<String> ownedDLCPersonaIDs;
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

        if(personaOne.rare || personaTwo.rare){
            if(!rarePersonaAllowedInFusion){
                return null;
            }

            if(personaOne.rare){
                return fuseRare(personaTwo, personaOne);
            }

            //persona two has to be rare
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
        if(persona.dlc){
            return ownedDLCPersonaIDs.contains(persona.id);
        }

        if(persona.rare){
            return rarePersonaAllowedInFusion;
        }

        return true;
    }

    private boolean personaIsValidInFusionResult(Persona persona){
        final boolean validInFusion = !persona.rare && !persona.special;

        if(validInFusion && persona.dlc){
            return ownedDLCPersonaIDs.contains(persona.id);
        }

        return validInFusion;
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
