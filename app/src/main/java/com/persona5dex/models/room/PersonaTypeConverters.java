package com.persona5dex.models.room;

import android.arch.persistence.room.TypeConverter;

import com.persona5dex.models.Enumerations;

/**
 * Created by Rechee on 11/18/2017.
 */

public class PersonaTypeConverters {
    @TypeConverter
    public static int toInt(Enumerations.Arcana arcana){
        if(arcana == null){
            return 0;
        }

        return arcana.ordinal();
    }

    @TypeConverter
    public static Enumerations.Arcana toArcana(int arcana){
        try{
            return Enumerations.Arcana.values()[arcana];
        }
        catch (IndexOutOfBoundsException e){
            return Enumerations.Arcana.CHARIOT;
        }
    }

    @TypeConverter
    public static int toInt(Enumerations.ElementEffect elementEffect){
        if(elementEffect == null){
            return 0;
        }

        return elementEffect.ordinal();
    }

    @TypeConverter
    public static Enumerations.ElementEffect toElementEffect(int elementEffect){
        try{
            return Enumerations.ElementEffect.values()[elementEffect];
        }
        catch (IndexOutOfBoundsException e){
            return Enumerations.ElementEffect.DRAIN;
        }
    }
}
