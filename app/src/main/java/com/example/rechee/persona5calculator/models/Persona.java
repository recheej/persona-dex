package com.example.rechee.persona5calculator.models;

import com.example.rechee.persona5calculator.PersonaUtilities;
import com.example.rechee.persona5calculator.models.Enumerations.Arcana;
import com.example.rechee.persona5calculator.models.Enumerations.ElementEffect;
import com.example.rechee.persona5calculator.models.Enumerations.Element;
import com.example.rechee.persona5calculator.models.Enumerations.Personality;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

/**
 * Created by Rechee on 7/1/2017.
 */

public class Persona extends BasePersona {

    Stats stats;
    HashMap<Element, ElementEffect> elements;
    Personality personality;

    private Skill[] personaSkills;

    public Arcana arcana;

    public Persona() {
        this.elements = new HashMap<>();
        personality = Personality.UNKNOWN;
    }

    public Skill[] getPersonaSkills() {
        return personaSkills;
    }

    public Stats getStats() {
        return this.stats;
    }

    public HashMap<Element, ElementEffect> getElements() {
        return this.elements;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Persona){
            Persona otherPersona = (Persona) obj;
            return Objects.equals(otherPersona.name, this.name);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }

    public Arcana getArcana() {
        return arcana;
    }

    public static class Stats {
        public final int ENDURANCE;
        public final int AGILITY;
        public final int STRENGTH;
        public final int MAGIC;
        public final int LUCK;

        Stats(int[] rawStats){
            STRENGTH = rawStats[0];
            MAGIC = rawStats[1];
            ENDURANCE = rawStats[2];
            AGILITY = rawStats[3];
            LUCK = rawStats[4];
        }
    }

    public static Persona mapFromRawPersona(RawPersona rawPersona){
        Persona persona = new Persona();
        persona.name = rawPersona.name;
        persona.level = rawPersona.level;
        persona.special = rawPersona.special;
        persona.max = rawPersona.max;
        persona.dlc = rawPersona.dlc;
        persona.rare = rawPersona.rare;
        persona.arcanaName = rawPersona.arcana;
        persona.stats = new Persona.Stats(rawPersona.stats);

        persona.mapSkills(rawPersona.skills);

        Element[] elements = new Element[] {Element.PHYSICAL, Element.GUN, Element.FIRE, Element.ICE, Element.ELECTRIC,
                Element.WIND, Element.PSYCHIC, Element.NUCLEAR, Element.BLESS, Element.CURSE };

        for(int i = 0; i < rawPersona.elems.length; i++){
            String elementString = rawPersona.elems[i];
            Element element = elements[i];

            ElementEffect effect = ElementEffect.NO_EFFECT;

            switch (elementString) {
                case "wk":
                    effect = ElementEffect.WEAK;
                    break;
                case "-":
                    effect = ElementEffect.NO_EFFECT;
                    break;
                case "rs":
                    effect = ElementEffect.RESIST;
                    break;
                case "nu":
                    effect = ElementEffect.NULL;
                    break;
                case "rp":
                    effect = ElementEffect.REPEL;
                    break;
                case "ab":
                    effect = ElementEffect.DRAIN;
                    break;
            }

            persona.elements.put(element, effect);
        }

        if(rawPersona.personality != null && !Objects.equals(rawPersona.personality, "") && !Objects.equals(rawPersona.personality, " ")){
            switch (rawPersona.personality){
                case "Timid":
                    persona.personality = Personality.TIMID;
                    break;
                case "Upbeat":
                    persona.personality = Personality.UPBEAT;
                    break;
                case "Gloomy":
                    persona.personality = Personality.GLOOMY;
                    break;
                case "Irritable":
                    persona.personality = Personality.IRRITABLE;
                    break;
            }
        }

        String rawArcanaFormatted = rawPersona.arcana.replaceAll("\\s+", "").toLowerCase();

        HashMap<String, Arcana> arcanaHashMap = PersonaUtilities.arcanaHashMap();

        if(arcanaHashMap.containsKey(rawArcanaFormatted)){
            persona.arcana = arcanaHashMap.get(rawArcanaFormatted);
        }

        return persona;
    }

    private void mapSkills(HashMap<String, Integer> baseSkills){
        this.personaSkills = new Skill[baseSkills.size()];

        int i = 0;
        for (Map.Entry<String, Integer> entry : baseSkills.entrySet()) {
            Skill newSkill = new Skill();
            newSkill.setName(entry.getKey());
            newSkill.setLevel(entry.getValue());
            personaSkills[i] = newSkill;
            i += 1;
        }
    }
}
