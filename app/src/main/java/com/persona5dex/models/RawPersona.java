package com.persona5dex.models;

import com.persona5dex.PersonaUtilities;
import com.persona5dex.models.Enumerations.Arcana;
import com.persona5dex.models.Enumerations.Element;
import com.persona5dex.models.Enumerations.ElementEffect;
import com.persona5dex.models.Enumerations.Personality;

import java.util.HashMap;
import java.util.Objects;

/**
 * Created by Rechee on 7/1/2017.
 */

public class RawPersona extends BasePersona {
    public String arcana;
    public String personality;
    public int[] stats;
    public String[] elems;

    public Persona toPersona() {
        BasePersona basePersona = (BasePersona) this;
        Persona persona = (Persona) basePersona;
        persona.stats = new Persona.Stats(stats);

        Element[] elements = new Element[] {Element.PHYSICAL, Element.GUN, Element.FIRE, Element.ICE, Element.ELECTRIC,
                Element.WIND, Element.PSYCHIC, Element.NUCLEAR, Element.BLESS, Element.CURSE };

        for(int i = 0; i < elems.length; i++){
            String elementString = elems[i];
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

        if(!Objects.equals(this.personality, "") && !Objects.equals(this.personality, " ")){
            switch (personality){
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

        String rawArcanaFormatted = this.arcana.replaceAll("\\s+", "").toLowerCase();

        HashMap<String, Arcana> arcanaHashMap = PersonaUtilities.arcanaHashMap();

        if(arcanaHashMap.containsKey(rawArcanaFormatted)){
            persona.arcana = arcanaHashMap.get(rawArcanaFormatted);
        }

        return persona;
    }
}
