package com.example.rechee.persona5calculator.dagger;

import android.content.Context;

import com.google.gson.Gson;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Rechee on 6/11/2017.
 */

@Module
public class ApplicationContextModule {

    private final Context context;

    public ApplicationContextModule(Context context){
        this.context = context;
    }

    @Provides
    @ApplicationScope
    @Named("applicationContext")
    public Context providesContext() {
        return context;
    }

    @Provides
    @ApplicationScope
    @Named("applicationGson")
    Gson gson() {
        return new Gson();
    }
}
