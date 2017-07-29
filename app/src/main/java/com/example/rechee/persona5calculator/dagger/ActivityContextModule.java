package com.example.rechee.persona5calculator.dagger;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.rechee.persona5calculator.PersonaUtilities;
import com.google.gson.Gson;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Rechee on 6/11/2017.
 */

@Module
public class ActivityContextModule {

    private final Context context;

    public ActivityContextModule(Context context){
        this.context = context;
    }

    @Provides
    @Named("activityContext")
    public Context providesContext() {
        return context;
    }

    @Provides
    @Named("transferSharedPreferences")
    SharedPreferences sharedPreferences(){
        return context.getSharedPreferences(PersonaUtilities.SHARED_PREF_TRANSFER_CONTENT, Context.MODE_PRIVATE);
    }
}
