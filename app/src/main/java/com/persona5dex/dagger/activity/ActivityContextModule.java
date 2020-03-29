package com.persona5dex.dagger.activity;

import android.content.Context;
import android.content.SharedPreferences;

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

    @ActivityScope
    @Provides
    @Named("transferSharedPreferences")
    SharedPreferences sharedPreferences(@Named("activityContext") Context context) {
        return context.getSharedPreferences(PersonaUtilities.SHARED_PREF_TRANSFER_CONTENT, Context.MODE_PRIVATE);
    }

    @Provides
    @ActivityScope
    @Named("dlcSharedPreferences")
    SharedPreferences dlcSharedPreferences(@Named("activityContext") Context context) {
        return context.getSharedPreferences(PersonaUtilities.SHARED_PREF_DLC, Context.MODE_PRIVATE);
    }

    @Provides
    @ActivityScope
    @Named("dlcPrefKey")
    String dlcprefKey(@Named("activityContext") Context context) {
        return context.getString(R.string.pref_key_dlc);
    }

    @Provides
    @ActivityScope
    @Named("rarePersonaInFusionKey")
    String rarePersonaInFusion(@Named("activityContext") Context context) {
        return context.getString(R.string.pref_key_rarePersona);
    }
}
