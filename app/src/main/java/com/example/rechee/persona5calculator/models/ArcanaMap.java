package com.example.rechee.persona5calculator.models;

/**
 * Created by Rechee on 8/14/2017.
 */

public class ArcanaMap {
    public String name;
    public Enumerations.Arcana arcana;

    @Override
    public String toString() {
        if(this.name.toLowerCase().contains("hanged")){
            return "Hanged Man";
        }

        return this.name.substring(0, 1).toUpperCase() + this.name.substring(1).toLowerCase();
    }
}
