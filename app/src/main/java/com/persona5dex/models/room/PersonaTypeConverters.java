package com.persona5dex.models.room;

import androidx.room.TypeConverter;

import com.persona5dex.models.Enumerations;
import com.persona5dex.models.GameType;

/**
 * Created by Rechee on 11/18/2017.
 */

public class PersonaTypeConverters {
    @TypeConverter
    public static int toInt(Enumerations.Arcana arcana) {
        if(arcana == null) {
            return 0;
        }

        return arcana.value();
    }

    @TypeConverter
    public static Enumerations.Arcana toArcana(int arcana) {

        for(Enumerations.Arcana arcana1 : Enumerations.Arcana.values()) {
            if(arcana1.value() == arcana) {
                return arcana1;
            }
        }

        return Enumerations.Arcana.ANY;
    }

    @TypeConverter
    public static int toInt(Enumerations.ElementEffect elementEffect) {
        if(elementEffect == null) {
            return 0;
        }

        return elementEffect.value();
    }

    @TypeConverter
    public static int toInt(boolean bool) {
        return bool ? 1 : 0;
    }

    @TypeConverter
    public static boolean toBool(int intToConvert) {
        return intToConvert == 1;
    }

    @TypeConverter
    public static Enumerations.ElementEffect toElementEffect(int elementEffect) {
        for(Enumerations.ElementEffect effect : Enumerations.ElementEffect.values()) {
            if(effect.value() == elementEffect) {
                return effect;
            }
        }

        return Enumerations.ElementEffect.WEAK;
    }

    @TypeConverter
    public static Enumerations.Element toElement(int element) {
        for(Enumerations.Element element1 : Enumerations.Element.values()) {
            if(element1.value() == element) {
                return element1;
            }
        }

        return Enumerations.Element.PHYSICAL;
    }

    @TypeConverter
    public static GameType toGameType(int gameType) {
        return GameType.getGameType(gameType);
    }

    @TypeConverter
    public static int toInt(GameType gameType) {
        return gameType.getValue();
    }

    @TypeConverter
    public static int toInt(Enumerations.Element element) {
        return element.value();
    }
}
