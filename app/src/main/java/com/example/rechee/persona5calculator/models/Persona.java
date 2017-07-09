package com.example.rechee.persona5calculator.models;

import com.example.rechee.persona5calculator.models.Enumerations.Arcana;
import com.example.rechee.persona5calculator.models.Enumerations.ElementEffect;
import com.example.rechee.persona5calculator.models.Enumerations.Element;
import com.example.rechee.persona5calculator.models.Enumerations.Personality;

import java.util.HashMap;
import java.util.Objects;

/**
 * Created by Rechee on 7/1/2017.
 */

public class Persona extends BasePersona {

    Stats stats;
    HashMap<Element, ElementEffect> elements;
    Personality personality;
    Arcana arcana;

    public Persona() {
        this.elements = new HashMap<>();
        personality = Personality.UNKNOWN;
    }

    static class Stats {
        final int ENDURANCE;
        final int AGILITY;
        final int STRENGTH;
        final int MAGIC;
        final int LUCK;

        public Stats(int[] rawStats){
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
        persona.skills = rawPersona.skills;
        persona.arcanaName = rawPersona.arcana;
        persona.stats = new Persona.Stats(rawPersona.stats);

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

        HashMap<String, Arcana> arcanaHashMap = new HashMap<>();
        for(Arcana arcana: Arcana.values()){
            String arcanaStringFormatted = arcana.name().replaceAll("\\s+", "")
                    .replaceAll("_", "").toLowerCase();

            arcanaHashMap.put(arcanaStringFormatted, arcana);
        }

        if(arcanaHashMap.containsKey(rawArcanaFormatted)){
            persona.arcana = arcanaHashMap.get(rawArcanaFormatted);
        }

        return persona;
    }
}
