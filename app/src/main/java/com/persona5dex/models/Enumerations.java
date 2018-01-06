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

        public String getName() {
            switch (i){
                case 0:
                    return "Fool";
                case 1:
                    return "Magician";
                case 2:
                    return "Priestess";
                case 3:
                    return "Empress";
                case 4:
                    return "Emperor";
                case 5:
                    return "Hierophant";
                case 6:
                    return "Lovers";
                case 7:
                    return "Chariot";
                case 8:
                    return "Justice";
                case 9:
                    return "Hermit";
                case 10:
                    return "Fortune";
                case 11:
                    return "Hanged Man";
                case 12:
                    return "Death";
                case 13:
                    return "Temperance";
                case 14:
                    return "Devil";
                case 15:
                    return "Tower";
                case 16:
                    return "Star";
                case 17:
                    return "Moon";
                case 18:
                    return "Sun";
                case 19:
                    return "Judgement";
                case 20:
                    return "World";
                case 21:
                    return "Strength";
                case -1:
                    return "Any";
                 default:
                     return "Any";
            }
        }

        public static Arcana getArcana(int i) {
            for (Arcana arcana : Arcana.values()) {
                if (arcana.value() == i) {
                    return arcana;
                }
            }

            return Arcana.ANY;
        }

        private static String formatArcanaName(String arcanaName) {
            if (arcanaName == null || arcanaName.isEmpty()) {
                return "";
            }

            return arcanaName.replaceAll("\\s+", "")
                    .replaceAll("_", "").toLowerCase();
        }


        public static Arcana getArcana(String arcanaName) {
            String arcanaStringFormatted = formatArcanaName(arcanaName);

            for (Arcana arcana : Arcana.values()) {
                if (formatArcanaName(arcana.getName()).equals(arcanaStringFormatted)) {
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

    public enum SearchResultType {
        PERSONA(1),
        SKILL(2);

        private int i;

        SearchResultType(final int value) {
            i = value;
        }

        public int value() {
            return i;
        }

        public static SearchResultType getSearchResultType(int value){
            for (SearchResultType searchResultType : SearchResultType.values()) {
                if(searchResultType.value() == value){
                    return searchResultType;
                }
            }

            return SearchResultType.PERSONA;
        }
    }


}
