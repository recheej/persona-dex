package com.persona5dex.services;

import androidx.annotation.Nullable;

import android.util.SparseArray;

import com.persona5dex.models.Enumerations.Arcana;
import com.persona5dex.models.PersonaForFusionService;
import com.persona5dex.models.room.Persona;

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

    private final PersonaForFusionService[] personasByLevel;
    private final boolean rarePersonaAllowedInFusion;
    private final Set<Integer> ownedDLCPersonaIDs;
    private SparseArray<List<PersonaForFusionService>> personaByArcana;
    private HashMap<Arcana, HashMap<Arcana, Arcana>> arcanaTable;
    private Map<String, int[]> rareComboMap;

    private final String[] rarePersonas = {"Regent", "Queen's Necklace", "Stone of Scone",
            "Koh-i-Noor", "Orlov", "Emperor's Amulet", "Hope Diamond", "Crystal Skull"};

    public PersonaFuser(PersonaFusionArgs args) {
        this.arcanaTable = args.arcanaTable;
        this.personasByLevel = args.personasByLevel;
        this.rareComboMap = args.rareComboMap;
        this.rarePersonaAllowedInFusion = args.rarePersonaAllowedInFusion;
        this.ownedDLCPersonaIDs = convertIDsToIntegers(args.ownedDLCPersonaIDs);
        this.personaByArcana = this.personaByArcana();
    }

    private Set<Integer> convertIDsToIntegers(Set<String> ids) {
        final int setSize = ids.size();
        Set<Integer> integerSet = new HashSet<>(setSize);
        for (String s : ids.toArray(new String[setSize])) {
            integerSet.add(Integer.parseInt(s));
        }

        return integerSet;
    }

    public static class PersonaFusionArgs {
        public PersonaForFusionService[] personasByLevel;
        public HashMap<Arcana, HashMap<Arcana, Arcana>> arcanaTable;
        public Map<String, int[]> rareComboMap;
        public boolean rarePersonaAllowedInFusion;
        public Set<String> ownedDLCPersonaIDs;
    }

    private int getRarePersonaIndex(String personaName) {
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
    private PersonaForFusionService fuseRare(PersonaForFusionService normalPersona, PersonaForFusionService rarePersona) {

        int rarePersonaIndex = this.getRarePersonaIndex(rarePersona.getName());
        int modifier = this.rareComboMap.get(normalPersona.getArcanaName())[rarePersonaIndex];

        List<PersonaForFusionService> personasOfSameArcana = personaByArcana.get(normalPersona.getArcana().value());

        int personaIndex = 0;
        final int arcanaSize = personasOfSameArcana.size();

        for (int i = 0; i < arcanaSize; i++) {
            PersonaForFusionService otherPersona = personasOfSameArcana.get(i);

            if (otherPersona.getName().equals(normalPersona.getName())) {
                personaIndex = i;
                break;
            }
        }

        int newPersonaIndex = personaIndex + modifier;
        PersonaForFusionService newPersona = null;
        if (newPersonaIndex >= 0 && newPersonaIndex < personasOfSameArcana.size()) {
            newPersona = personasOfSameArcana.get(newPersonaIndex);
            if(newPersona != null && !personaIsValidInFusionResult(newPersona)){
                newPersona = null;
            }

            //if the result isn't valid, loop through until we get a valid one
            while (newPersona != null && newPersona.isSpecial() && this.personaIsValidInFusionResult(newPersona)) {
                if (modifier > 0) {
                    modifier++;
                } else if (modifier < 0) {
                    modifier--;
                }

                newPersonaIndex = personaIndex + modifier;

                if (newPersonaIndex >= 0 && newPersonaIndex < personasOfSameArcana.size()) {
                    newPersona = personasOfSameArcana.get(newPersonaIndex);
                }
            }
        }

        return newPersona;
    }

    private SparseArray<List<PersonaForFusionService>> personaByArcana() {
        SparseArray<List<PersonaForFusionService>> personaByArcana = new SparseArray<>(arcanaTable.size());
        for (PersonaForFusionService persona : personasByLevel) {
            int arcanaIndex = persona.getArcana().value();
            List<PersonaForFusionService> personaList = personaByArcana.get(arcanaIndex);

            if (personaList == null) {
                personaList = new ArrayList<>();
                personaList.add(persona);
                personaByArcana.put(persona.getArcana().value(), personaList);
            } else {
                personaList.add(persona);
            }
        }

        return personaByArcana;
    }

    @Nullable
    public PersonaForFusionService fuseNormal(PersonaForFusionService personaOne, PersonaForFusionService personaTwo) {
        if (personaOne == personaTwo) {
            return null;
        }

        if (!personaIsValidInRecipe(personaOne) || !personaIsValidInRecipe(personaTwo)) {
            return null;
        }

        if (personaOne.isRare() && personaTwo.isRare()) {
            return null;
        }

        if (personaOne.isRare() || personaTwo.isRare()) {
            if (!rarePersonaAllowedInFusion) {
                return null;
            }

            if (personaOne.isRare()) {
                return fuseRare(personaTwo, personaOne);
            }

            //persona two has to be rare
            return fuseRare(personaOne, personaTwo);
        }

        Arcana resultArcana;
        if (personaOne.getArcana() == personaTwo.getArcana()) {
            resultArcana = personaOne.getArcana();
        } else {
            resultArcana = getResultArcana(personaOne.getArcana(), personaTwo.getArcana(), arcanaTable);
        }

        if (resultArcana == null) {
            return null;
        }

        int calculatedLevel = (personaOne.getLevel() + personaTwo.getLevel()) / 2;
        calculatedLevel += 1;

        List<PersonaForFusionService> personaForResultArcana = personaByArcana.get(resultArcana.value());

        if (personaForResultArcana.size() == 0) {
            //this should never happen, but hey you never know
            return null;
        }

        if (personaOne.getArcana() == personaTwo.getArcana()) {
            //fusion theory according to this: http://persona4.wikidot.com/fusiontutor
            //https://github.com/chinhodado/persona5_calculator/blob/master/src/FusionCalculator.js
            //https://www.gamefaqs.com/ps2/932312-shin-megami-tensei-persona-3/faqs/49926

            for (int i = personaForResultArcana.size() - 1; i >= 0; i--) {
                PersonaForFusionService persona = personaForResultArcana.get(i);

                if (persona.getLevel() < calculatedLevel) {
                    if (!this.personaIsValidInFusionResult(persona) || Objects.equals(persona.getName(), personaOne.getName()) || Objects.equals(persona.getName(), personaTwo.getName())) {
                        continue;
                    }

                    return persona;
                }
            }

            return null;
        } else {
            for (PersonaForFusionService persona : personaForResultArcana) {
                if (persona.getLevel() >= calculatedLevel) {
                    if (!this.personaIsValidInFusionResult(persona) || persona.getName().equals(personaOne.getName()) || persona.getName().equals(personaTwo.getName())) {
                        continue;
                    }

                    return persona;
                }
            }

            return null;
        }
    }

    private boolean personaIsValidInRecipe(PersonaForFusionService persona) {
        if (persona.isDlc()) {
            return ownedDLCPersonaIDs.contains(persona.getId());
        }

        if (persona.isRare()) {
            return rarePersonaAllowedInFusion;
        }

        return true;
    }

    private boolean personaIsValidInFusionResult(PersonaForFusionService persona) {
        final boolean validInFusion = !persona.isRare() && !persona.isSpecial();

        if (validInFusion && persona.isDlc()) {
            return ownedDLCPersonaIDs.contains(persona.getId());
        }

        return validInFusion;
    }

    @Nullable
    private Arcana getResultArcana(Arcana arcanaOne, Arcana arcanaTwo, HashMap<Arcana, HashMap<Arcana, Arcana>> arcanaTable) {
        if (arcanaTable.containsKey(arcanaOne)) {
            HashMap<Arcana, Arcana> innerTable = arcanaTable.get(arcanaOne);
            if (innerTable.containsKey(arcanaTwo)) {
                return innerTable.get(arcanaTwo);
            }
        }

        return null;
    }
}
