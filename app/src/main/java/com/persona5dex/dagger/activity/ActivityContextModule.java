package com.persona5dex.dagger.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.persona5dex.PersonaUtilities;
import com.persona5dex.R;

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

    @ActivityScope
    @Provides
    @Named("transferSharedPreferences")
    SharedPreferences sharedPreferences(){
        return context.getSharedPreferences(PersonaUtilities.SHARED_PREF_TRANSFER_CONTENT, Context.MODE_PRIVATE);
    }

    @Provides
    @ActivityScope
    @Named("fusionSharedPreferences")
    SharedPreferences fusionSharedPreferences(){
        return context.getSharedPreferences(PersonaUtilities.SHARED_PREF_FUSIONS, Context.MODE_PRIVATE);
    }

    @Provides
    @ActivityScope
    @Named("dlcSharedPreferences")
    SharedPreferences dlcSharedPreferences(){
        return context.getSharedPreferences(PersonaUtilities.SHARED_PREF_DLC, Context.MODE_PRIVATE);
    }

    @Provides
    @ActivityScope
    @Named("defaultSharedPreferences")
    SharedPreferences defaultSharedPreferences(){
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Provides
    @ActivityScope
    @Named("dlcPrefKey")
    String dlcprefKey(){
        return context.getString(R.string.pref_key_dlc);
    }

    @Provides
    @ActivityScope
    @Named("rarePersonaInFusionKey")
    String rarePersonaInFusion(){
        return context.getString(R.string.pref_key_rarePersona);
    }
}
