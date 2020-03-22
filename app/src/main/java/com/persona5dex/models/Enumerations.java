package com.persona5dex.models;

import android.content.Context;

import androidx.annotation.StringRes;

import com.persona5dex.R;

/**
 * Created by Rechee on 7/1/2017.
 */

public class Enumerations {
    public enum Arcana {
        FOOL(0, R.string.arcana_fool),
        MAGICIAN(1, R.string.arcana_magician),
        PRIESTESS(2, R.string.arcana_priestess),
        EMPRESS(3, R.string.arcana_empress),
        EMPEROR(4, R.string.arcana_emperor),
        HIEROPHANT(5, R.string.arcana_hierophant),
        LOVERS(6, R.string.arcana_lovers),
        CHARIOT(7, R.string.arcana_chariot),
        JUSTICE(8, R.string.arcana_justice),
        HERMIT(9, R.string.arcana_hermit),
        FORTUNE(10, R.string.arcana_fortune),
        HANGED_MAN(11, R.string.arcana_hanged_man),
        DEATH(12, R.string.arcana_death),
        TEMPERANCE(13, R.string.arcana_temperance),
        DEVIL(14, R.string.arcana_devil),
        TOWER(15, R.string.arcana_tower),
        STAR(16, R.string.arcana_star),
        MOON(17, R.string.arcana_moon),
        SUN(18, R.string.arcana_sun),
        JUDGEMENT(19, R.string.arcana_judgement),
        WORLD(20, R.string.arcana_world),
        STRENGTH(21, R.string.arcana_strength),
        FAITH(22, R.string.arcana_faith),
        CONSULTANT(23, R.string.arcana_consultant),
        ANY(-1, R.string.arcana_any);

        private int i;
        private final int textRes;

        Arcana(int value, @StringRes int textRes) {
            i = value;
            this.textRes = textRes;
        }

        @StringRes public int getTextRes() {
            return textRes;
        }

        public int value() {
            return i;
        }

        public static Arcana getArcana(int i) {
            for (Arcana arcana : Arcana.values()) {
                if (arcana.value() == i) {
                    return arcana;
                }
            }

            return Arcana.ANY;
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
