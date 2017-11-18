package com.persona5dex.services;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.JobIntentService;

import com.persona5dex.PersonaUtilities;
import com.persona5dex.models.Enumerations;
import com.persona5dex.models.RawPersona;
import com.persona5dex.models.RawSkill;
import com.persona5dex.models.room.Persona;
import com.persona5dex.models.room.PersonaDao;
import com.persona5dex.models.room.PersonaDatabase;
import com.persona5dex.models.room.PersonaElement;
import com.persona5dex.models.room.PersonaSkill;
import com.persona5dex.models.room.Skill;
import com.persona5dex.models.room.Stats;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Rechee on 11/18/2017.
 */

public class CreateDatabaseJobService extends JobIntentService {
    private static final int JOB_ID = 1001;

    @Inject
    RawPersona[] rawPersonas;

    @Inject
    RawSkill[] rawSkills;

    @Inject
    PersonaDatabase personaDatabase;

    private PersonaDao dao;
    private HashMap<String, Integer> nameMap;

    @Override
    protected void onHandleWork(@NonNull Intent intent) {

        dao = personaDatabase.personaDao();

        HashMap<String, Enumerations.Arcana> arcanaHashMap = PersonaUtilities.arcanaHashMap();
        nameMap = new HashMap<>(rawPersonas.length);

        Persona[] personasToInsert = new Persona[rawPersonas.length];

        List<PersonaElement> newPersonaElements = new ArrayList<>(1000);
        for (int i1 = 0; i1 < rawPersonas.length; i1++) {
            RawPersona rawPersona = rawPersonas[i1];

            Persona newPersona = new Persona();
            newPersona.id = i1;

            newPersona.name = rawPersona.name;
            newPersona.level = rawPersona.level;
            newPersona.personality = rawPersona.personality;
            newPersona.note = rawPersona.note;
            newPersona.stats = new Stats(
                    rawPersona.stats[2],
                    rawPersona.stats[3],
                    rawPersona.stats[0],
                    rawPersona.stats[1],
                    rawPersona.stats[4]
            );

            //special flags
            newPersona.special = rawPersona.special;
            newPersona.max = rawPersona.max;
            newPersona.dlc = rawPersona.dlc;
            newPersona.rare = rawPersona.rare;

            String rawArcanaFormatted = rawPersona.arcana
            .replaceAll("\\s+", "")
            .replaceAll("_", "")
            .toLowerCase();

            if (arcanaHashMap.containsKey(rawArcanaFormatted)) {
                newPersona.arcana = arcanaHashMap.get(rawArcanaFormatted);
            }

            nameMap.put(newPersona.name.toLowerCase(), i1);

            personasToInsert[i1] = newPersona;

            newPersonaElements.addAll(getPersonaElements(rawPersona, i1));
        }

        dao.insertPersonas(personasToInsert);
        dao.insertPersonaElements(newPersonaElements);
        insertPersonaSkills();
    }

    private void insertPersonaSkills() {

        Skill[] newSkills = new Skill[rawSkills.length];
        List<PersonaSkill> newPersonaSkills = new ArrayList<>(7 * rawSkills.length);
        for (int i = 0; i < rawSkills.length; i++) {
            RawSkill rawSkill = rawSkills[i];

            Skill newSkill = new Skill();

            newSkill.id = i;
            newSkill.name = rawSkill.name;
            newSkill.effect = rawSkill.effect;
            newSkill.element = rawSkill.element;
            newSkill.cost = rawSkill.cost;
            newSkill.note = rawSkill.note;

            for (RawSkill.PersonaForSkill persona : rawSkill.personas) {
                final String personaNameFormatted = persona.name.toLowerCase();
                if(nameMap.containsKey(personaNameFormatted)){
                    int personaID = nameMap.get(personaNameFormatted);
                    newPersonaSkills.add(new PersonaSkill(personaID, i, persona.levelRequired));
                }
            }

            newSkills[i] = newSkill;
        }

        dao.insertSkills(newSkills);
        dao.insertPersonaSkills(newPersonaSkills);
    }

    private List<PersonaElement> getPersonaElements(RawPersona rawPersona, int i){

        Enumerations.Element[] elements = new Enumerations.Element[]{Enumerations.Element.PHYSICAL, Enumerations.Element.GUN, Enumerations.Element.FIRE, Enumerations.Element.ICE, Enumerations.Element.ELECTRIC,
                Enumerations.Element.WIND, Enumerations.Element.PSYCHIC, Enumerations.Element.NUCLEAR, Enumerations.Element.BLESS, Enumerations.Element.CURSE};

        List<PersonaElement> personaElementsToInsert = new ArrayList<>(elements.length);
        for (int j = 0; j < rawPersona.elems.length; j++) {
            String elementString = rawPersona.elems[j];
            Enumerations.Element element = elements[j];

            Enumerations.ElementEffect effect = Enumerations.ElementEffect.NO_EFFECT;

            switch (elementString) {
                case "wk":
                    effect = Enumerations.ElementEffect.WEAK;
                    break;
                case "-":
                    effect = Enumerations.ElementEffect.NO_EFFECT;
                    break;
                case "rs":
                    effect = Enumerations.ElementEffect.RESIST;
                    break;
                case "nu":
                    effect = Enumerations.ElementEffect.NULL;
                    break;
                case "rp":
                    effect = Enumerations.ElementEffect.REPEL;
                    break;
                case "ab":
                    effect = Enumerations.ElementEffect.DRAIN;
                    break;
            }

            PersonaElement newPersonaElement = new PersonaElement(i, element, effect);
            personaElementsToInsert.add(newPersonaElement);
        }

        return personaElementsToInsert;
    }
}
