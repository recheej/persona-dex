package com.example.rechee.persona5calculator.dagger;

import android.content.Context;

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

    @ActivityScope
    @Provides
    @Named("activityContext")
    public Context providesContext() {
        return context;
    }
}
