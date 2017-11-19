package com.persona5dex.models;

/**
 * Created by Rechee on 7/1/2017.
 */

public class Enumerations {
    public enum Arcana {
        FOOL(0),
        MAGICIAN(1),
        PRIESTESS(2),
        EMPRESS(3),
        EMPEROR(4),
        HIEROPHANT(5),
        LOVERS(6),
        CHARIOT(7),
        JUSTICE(8),
        HERMIT(9),
        FORTUNE(10),
        HANGED_MAN(11),
        DEATH(12),
        TEMPERANCE(13),
        DEVIL(14),
        TOWER(15),
        STAR(16),
        MOON(17),
        SUN(18),
        JUDGEMENT(19),
        WORLD(20),
        STRENGTH(21),
        ANY(-1);

        private int i;
        Arcana(int value) {
            i = value;
        }

        public int value() {
            return i;
        }

        public static Arcana getArcana(int i){
            for (Arcana arcana : Arcana.values()) {
                if(arcana.value() == i){
                    return arcana;
                }
            }

            return Arcana.ANY;
        }
    }

    public enum Personality {
        UPBEAT(0),
        TIMID(1),
        IRRITABLE(2),
        GLOOMY(3),
        UNKNOWN(4);

        private int i;

        Personality(int value) {
            i = value;
        }

        public int value() {
            return i;
        }
    }

    public enum Element {
        PHYSICAL(0),
        GUN(1),
        FIRE(2),
        ICE(3),
        ELECTRIC(4),
        WIND(5),
        PSYCHIC(6),
        NUCLEAR(7),
        BLESS(8),
        CURSE(9);

        private int i;

        Element(final int value) {
            i = value;
        }

        public int value() {
            return i;
        }
    }

    public enum ElementEffect {
        WEAK(0),
        RESIST(1),
        NULL(2),
        REPEL(3),
        DRAIN(4),
        NO_EFFECT(5);

        private int i;

        ElementEffect(final int value) {
            i = value;
        }

        public int value() {
            return i;
        }
    }
}
