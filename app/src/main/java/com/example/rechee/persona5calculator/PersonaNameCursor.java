package com.example.rechee.persona5calculator;

import android.app.SearchManager;
import android.database.AbstractCursor;
import android.provider.BaseColumns;

import com.example.rechee.persona5calculator.models.PersonaSearchSuggestion;

/**
 * Created by Rechee on 7/3/2017.
 */

public class PersonaNameCursor extends AbstractCursor {

    private final PersonaSearchSuggestion[] searchSuggestions;

    public PersonaNameCursor(PersonaSearchSuggestion[] searchSuggestions){
        this.searchSuggestions = searchSuggestions;
    }

    @Override
    public int getCount() {
        return this.searchSuggestions.length;
    }

    @Override
    public String[] getColumnNames() {
        return new String[] {BaseColumns._ID, SearchManager.SUGGEST_COLUMN_TEXT_1, SearchManager.SUGGEST_COLUMN_TEXT_2, SearchManager.SUGGEST_COLUMN_INTENT_DATA};
    }

    @Override
    public String getString(int column) {

        switch (column){
            case 0:
            case 1:
                return this.searchSuggestions[getPosition()].name;
            case 2:
                return this.searchSuggestions[getPosition()].arcana;
            case 3:
                return Integer.toString(getPosition());

        }

        return null;
    }

    @Override
    public short getShort(int column) {
        return 0;
    }

    @Override
    public int getInt(int column) {
        if(column == 0){
            return getPosition();
        }

        return 0;
    }

    @Override
    public long getLong(int column) {
        return 0;
    }

    @Override
    public float getFloat(int column) {
        return 0;
    }

    @Override
    public double getDouble(int column) {
        return 0;
    }

    @Override
    public boolean isNull(int column) {
        return this.searchSuggestions[getPosition()].name == null;
    }
}
