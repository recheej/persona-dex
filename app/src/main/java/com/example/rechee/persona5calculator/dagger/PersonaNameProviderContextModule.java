package com.example.rechee.persona5calculator.dagger;

import android.content.Context;

import com.example.rechee.persona5calculator.models.PersonaSearchSuggestion;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.Comparator;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Rechee on 7/1/2017.
 */

@Module
public class PersonaNameProviderContextModule {

    private final Context context;

    public PersonaNameProviderContextModule(Context context){
        this.context = context;
    }

    @PersonaNameProviderScope
    @Named("personaNameProviderContext")
    @Provides
    public Context context(){
        return this.context;
    }
}
